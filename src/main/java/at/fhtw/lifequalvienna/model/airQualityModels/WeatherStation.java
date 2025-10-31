package at.fhtw.lifequalvienna.model.airQualityModels;

import java.util.HashMap;
import java.util.Map;

public class WeatherStation {
    private String id;
    private String name;
    private double lat;
    private double lon;
    private Map<String, Double> data = new HashMap<String, Double>();

    public WeatherStation(String id, String name, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public void setDataItem(String dataName, double value) {
        this.data.put(dataName, value);
    }
    public  Map<String, Double> getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
