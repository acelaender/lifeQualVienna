package at.fhtw.lifequalvienna.model;

public enum UserType {
    STUDENT(0.10, 0.45, 0.3, 0.15),
    FAMILY(0.30, 0.15, 0.35, 0.20),
    PETOWNER(0.30, 0.15, 0.35, 0.20);

    public final double airQualityWeight;
    public final double transportWeight;
    public final double parksWeight;
    public final double policeWeight;

    UserType(double air, double transport, double parks, double police) {
        this.airQualityWeight = air;
        this.transportWeight = transport;
        this.parksWeight = parks;
        this.policeWeight = police;
    }
}
