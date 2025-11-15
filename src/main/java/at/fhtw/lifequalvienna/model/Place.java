package at.fhtw.lifequalvienna.model;

public class Place {
    private String adress;
    private String bezirk;
    private String municipality;
    private String category;
    private double x;
    private double y;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Place(String adress, String bezirk, String municipality, String category, double x, double y) {
        this.adress = adress;
        this.bezirk = bezirk;
        this.municipality = municipality;
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public String getAdress() {
        return adress;
    }

    public String getBezirk() {
        return bezirk;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getCategory() {
        return category;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
