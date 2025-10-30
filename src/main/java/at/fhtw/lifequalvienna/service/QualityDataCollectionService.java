package at.fhtw.lifequalvienna.service;


import at.fhtw.lifequalvienna.model.Station;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class QualityDataCollectionService {
    private final List<Station> stations = new ArrayList<Station>();

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
                    double lat = Double.parseDouble(line[4].trim());
                    double lon = Double.parseDouble(line[5].trim());
                    stations.add(new Station(id, name, lat, lon));
                } catch (Exception e) {
                    System.err.println("couldnt parse Oeffi-Station line: " + line[0] + ", name: " + line[1] + ", error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("couldnt parse Oeffi-Stations: ERROR: " + e.getMessage());
        }
    }

    public Station findNearest(double lat, double lon) {
        return stations.stream()
                .min(Comparator.comparingDouble(s -> distance(lat, lon, s.getLat(), s.getLon())))
                .orElse(null);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double a = Math.abs(lat2 - lat1);
        double b = Math.abs(lon2 - lon1);

        double res = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return res;
    }
    //public getOeffiDistance()

}
