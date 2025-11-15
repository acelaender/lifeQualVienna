package at.fhtw.lifequalvienna.model;

public class Response {
    private String adress;
    private String userType;
    private double grade;
    private String explanation;

    public Response(String adress, String userType, double grade, String explanation) {
        this.adress = adress;
        this.userType = userType;
        this.grade = grade;
        this.explanation = explanation;
    }

    public Response() {
    }

    public String getAdress() {
        return adress;
    }

    public String getUserType() {
        return userType;
    }

    public double getGrade() {
        return grade;
    }

    public String getExplanation() {
        return explanation;
    }
}
