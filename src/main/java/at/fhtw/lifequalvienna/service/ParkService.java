package at.fhtw.lifequalvienna.service;

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
public class ParkService {
    private static final String DATA_URL = "https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:OEFFGRUENPKTGOGD&srsName=EPSG:4326&outputFormat=csv";

    private final List<Station> parks = new ArrayList<>();
    private GeoService geoService = new GeoService();


    @PostConstruct
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
}
