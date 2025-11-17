package at.fhtw.lifequalvienna.service;

import at.fhtw.lifequalvienna.model.Place;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdressValidationService {
    private final WebClient webClient = WebClient.create("https://data.wien.gv.at/daten/OGDAddressService.svc");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeoService geoService = new GeoService();

    public List<Place> findPlace(String adress) {
        try {
            String encoded = URLEncoder.encode(adress, StandardCharsets.UTF_8);
            String url = "/GetAddressInfo?Address=" + adress;

            String responseRaw = webClient.get()
                    .uri(url)
                    .header("Accept", "application/json, text/plain, */*")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map<String, Object> response = objectMapper.readValue(responseRaw, Map.class);

            if (response == null || !response.containsKey("features")) {
                return List.of();
            }

            var features = (List<Map<String, Object>>) response.get("features");
            List<Place> suggestions = new ArrayList<>();

            for (Map<String, Object> feature : features) {
                Map<String, Object> properties = (Map<String, Object>) feature.get("properties");
                Map<String, Object> geometry = (Map<String, Object>) feature.get("geometry");

                if (properties == null || geometry == null) continue;

                var coords = (List<Double>) geometry.get("coordinates");
                double x;
                double y;
                if(coords != null && coords.size() >= 2){
                    ProjCoordinate coor =  GeoService.toWgs84(coords.get(0), coords.get(1));
                    x = coor.x;
                    y = coor.y;
                } else {
                    x = 0.0;
                    y = 0.0;
                }

                Place place = new Place(
                        (String) properties.get("Adresse"),
                        (String) properties.get("Bezirk"),
                        (String) properties.get("Municipality"),
                        (String) properties.get("Kategorie"),
                        x,
                        y
                );

                new ParkService();

                suggestions.add(place);
            }

            return suggestions;
        } catch (Exception e) {
            System.err.println("Fehler bei Adresssuche: " + e.getMessage());
            return List.of();
        }
    }
}
