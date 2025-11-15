package at.fhtw.lifequalvienna.service;

import at.fhtw.lifequalvienna.model.airQualityModels.Pollutant;
import at.fhtw.lifequalvienna.model.airQualityModels.WeatherStation;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class AirQualityService {
    private final List<WeatherStation> weatherStations = new ArrayList<>();
    private GeoService geoService;

    public AirQualityService(GeoService geoService) {
        this.geoService = geoService;
    }

    private static final List<Pollutant> POLL_WEIGHED = List.of(
            new Pollutant("PM25", 0.25, 10, 35), //Spaltenindex 16
            new Pollutant("PM10", 0.20, 20, 50), //Spaltenindex 13
            new Pollutant("NO2", 0.20, 25, 100), //Spaltenindex 9
            new Pollutant("O3", 0.15, 60, 180), //Spaltenindex 19
            new Pollutant("SO2", 0.10, 20, 75), //Spaltenindex 22
            new Pollutant("CO", 0.10, 1, 10) //Spaltenindex 25
    );

    private static final String DATA_URL = "https://go.gv.at/l9lumesakt";

    @PostConstruct
    public void loadWeatherStations() {
        try {
            String stationsUrl = "https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:LUFTGUETENETZOGD&srsName=EPSG:4326&outputFormat=csv";

            var reader = new CSVReaderBuilder(new InputStreamReader(new URL(stationsUrl).openStream(), "UTF-8"))
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                    .build();
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null){
                if((line.length != 29)){
                    System.err.println("Skipping line with id: " + line[0] + "; incorrect length");
                    continue;
                }

                try {
                    String id = line[0].trim();
                    String name = line[3].trim();
                    var coords = geoService.parsePoint(line[2]);
                    double lon = coords[0];
                    double lat = coords[1];
                    weatherStations.add(new WeatherStation(id, name, lat, lon));
                } catch (Exception e) {
                    System.err.println("couldnt parse Weather-Station line: " + line[0] + ", name: " + line[3] + ", error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("couldnt parse Weather-Stations: ERROR: " + e.getMessage());
        }
    }

    public Double getPollutionScore(double lat, double lon) {
        Map<String, Double> pollData = getPollutionData(lat, lon);

        double totalScore = 0.0;
        double totalWeight = 0.0;

        for(var entry : pollData.entrySet()){
            String pollutant = entry.getKey();
            double value = entry.getValue();

            if (value < 0.0) {
                //TODO this should only be visible in debug
                System.err.println("Pollutant " + pollutant + " has no data available at your station");
                continue;
            }

            double valueScore = 0.0;

            for(var poll : POLL_WEIGHED){
                if(pollutant.equals(poll.getName())){
                    valueScore = 1 - ((value - poll.getGood()) /  (poll.getBad() - poll.getGood()));
                    valueScore = Math.max(0, Math.min(1, valueScore));
                    totalScore += (valueScore * poll.getWeight());
                    totalWeight += poll.getWeight();
                }
            }
        }

        totalScore = (totalScore * 100) / totalWeight;
        System.out.println(totalScore);
        return totalScore;
    }

    private void loadPollutionData() {
        try {
            var reader = new CSVReaderBuilder(new InputStreamReader(new URL(DATA_URL).openStream(), "UTF-8"))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();
            reader.readNext();

            String[] line;

            while ((line = reader.readNext()) != null){
                if((line.length != 26)){
                    System.err.println("Skipping line with id: " + line[0] + "; incorrect length");
                    continue;
                }

                try {
                    String name = line[0].trim();

                    WeatherStation match = weatherStations.stream()
                            .filter(w -> w.getName().equalsIgnoreCase(name))
                            .findFirst()
                            .orElse(null);

//--------------------------------------------------Parsing the Data into the Weather Station List
                    if (match != null) {
                        match.setDataItem("NO2", parseDouble(line[9].trim()));
                        match.setDataItem("PM10", parseDouble(line[13].trim()));
                        match.setDataItem("PM25", parseDouble(line[16].trim()));
                        match.setDataItem("O3", parseDouble(line[19].trim()));
                        match.setDataItem("SO2", parseDouble(line[22].trim()));
                        match.setDataItem("CO", parseDouble(line[25].trim()));
//------------------------------------------------------------------------------------------------
                    } else {
                        System.err.println("No geometric data for weather station data: " + line[0].trim());
                    }
                } catch (Exception e) {
                    System.err.println("couldnt parse Weather-Station-Data line: " + line[0] + ", name: " + line[1] + ", error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("couldnt parse Weather-Station-Data: ERROR: " + e.getMessage());
        }
    }

    private double parseDouble(String value) {
        value = value.trim();
        if (value.equalsIgnoreCase("NE") || value.isEmpty()) return -1;
        return Double.parseDouble(value.replace(",", "."));
    }

    private Map<String, Double> getPollutionData(double lon, double lat) {
        loadPollutionData();
        WeatherStation nearestStation = getNearestStation(lon, lat);
        if(nearestStation != null){
            return nearestStation.getData();
        }else{
            System.err.println("There was a problem fetching pollution data");
            return null;
        }
    }

    private WeatherStation getNearestStation(double lon, double lat) {
        return weatherStations.stream()
                .min(Comparator.comparingDouble(s -> geoService.distance(lon, lat, s.getLon(), s.getLat())))
                .orElse(null);
    }
}
