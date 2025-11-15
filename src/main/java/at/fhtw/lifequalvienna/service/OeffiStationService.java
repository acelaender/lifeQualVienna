package at.fhtw.lifequalvienna.service;


import at.fhtw.lifequalvienna.model.Score;
import at.fhtw.lifequalvienna.model.Station;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OeffiStationService {
    private final List<Station> stations = new ArrayList<Station>();

    private final double MAX_DISTANCE = 500;

    private GeoService geoService;

    public OeffiStationService(GeoService geoService) {
        this.geoService = geoService;
    }

    @PostConstruct
    public void loadStations() {
        try {
            String stationsUrl = "https://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien-ogd-haltestellen.csv";

            var reader = new CSVReaderBuilder(new InputStreamReader(new URL(stationsUrl).openStream(), "UTF-8"))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null){
                if((line.length != 6)){
                    System.err.println("Skipping line with id: " + line[0] + "; incorrect length");
                    continue;
                }

                try {
                    String id = line[0].trim();
                    String name = line[1].trim();
                    double lon = Double.parseDouble(line[4].trim());
                    double lat = Double.parseDouble(line[5].trim());
                    stations.add(new Station(id, name, lat, lon));
                } catch (Exception e) {
                    System.err.println("couldnt parse Oeffi-Station line: " + line[0] + ", name: " + line[1] + ", error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("couldnt parse Oeffi-Stations: ERROR: " + e.getMessage());
        }
    }



    private Station findNearest(double lon, double lat) {
        return stations.stream()
                .min(Comparator.comparingDouble(s -> geoService.distance(lon, lat, s.getLon(), s.getLat())))
                .orElse(null);
    }

    public Score calculateScore(double lon, double lat) {
        Score score = new Score();
        Station nearestStation = findNearest(lon, lat);
        //TODO maybe check for null

        score.setExplanation("The nearest public transportation station is: " + nearestStation.getName());

        double distance = geoService.distance(lon, lat, nearestStation.getLon(), nearestStation.getLat());

        score.setExplanation(score.getExplanation() + ", which is " + distance + "m away. This results in a Public transpo score of: ");


        if(distance >= MAX_DISTANCE) {
            score.setScore(0.0);
        } else {
            double norm = 1.0 - (distance / MAX_DISTANCE);
            score.setScore(norm * 100);
        }

        score.setExplanation(score.getExplanation() + score.getScore() + "%");

        return score;
    }

}
