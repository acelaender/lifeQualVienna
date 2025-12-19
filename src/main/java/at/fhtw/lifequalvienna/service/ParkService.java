package at.fhtw.lifequalvienna.service;

import at.fhtw.lifequalvienna.model.Score;
import at.fhtw.lifequalvienna.model.Station;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ParkService {
    private static final String DATA_URL = "https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:OEFFGRUENPKTGOGD&srsName=EPSG:4326&outputFormat=csv";

    private final List<Station> parks = new ArrayList<>();
    private GeoService geoService = new GeoService();

    private final double MAX_DISTANCE = 1000;

    @PostConstruct
    public void init() {
        loadParks();
    }


    @Scheduled(fixedRate = 5 * 60 * 60 * 1000)
    public void loadParks() {
        try {

            var reader = new CSVReaderBuilder(new InputStreamReader(new URL(DATA_URL).openStream(), "UTF-8"))
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                    .build();
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null){
                if((line.length != 10)){
                    System.err.println("Skipping line with id: " + line[0] + "; incorrect length");
                    continue;
                }

                try {
                    String id = line[0].trim();
                    String name = line[6].trim();
                    var coords = geoService.parsePoint(line[2]);
                    double lon = coords[0];
                    double lat = coords[1];
                    parks.add(new Station(id, name, lat, lon));
                } catch (Exception e) {
                    System.err.println("couldnt parse Weather-Station line: " + line[0] + ", name: " + line[3] + ", error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("couldnt parse Weather-Stations: ERROR: " + e.getMessage());
        }
    }

    public Station findNearest(double lon, double lat) {
        return parks.stream()
                .min(Comparator.comparingDouble(s -> geoService.distance(lon, lat, s.getLon(), s.getLat())))
                .orElse(null);
    }

    public Score calculateScore(double lon, double lat) {
        Score score = new Score();
        Station nearestStation = findNearest(lon, lat);

        score.setExplanation("The nearest public park is: " + nearestStation.getName());

        double distance = geoService.distance(lon, lat, nearestStation.getLon(), nearestStation.getLat());

        score.setExplanation(score.getExplanation() + ", which is " + (int) distance + "m away. This results in a Park score of: ");


        if(distance >= MAX_DISTANCE) {
            score.setScore(0.0);
        } else {
            double norm = 1.0 - (distance / MAX_DISTANCE);
            score.setScore(norm * 100);
        }

        score.setExplanation(score.getExplanation() + (int) score.getScore() + "%");

        return score;
    }
}
