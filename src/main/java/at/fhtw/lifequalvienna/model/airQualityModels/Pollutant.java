package at.fhtw.lifequalvienna.model.airQualityModels;

public class Pollutant {
    private String name;
    private double weight;
    private double good;
    private double bad;

    public Pollutant(String name, double weight, double good, double bad) {
        this.name = name;
        this.weight = weight;
        this.good = good;
        this.bad = bad;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getGood() {
        return good;
    }

    public double getBad() {
        return bad;
    }
}
