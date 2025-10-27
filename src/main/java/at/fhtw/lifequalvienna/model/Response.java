package at.fhtw.lifequalvienna.model;

public class Response {
    private String adress;
    private String userType;
    private int grade;

    public Response(String adress, String userType, int grade) {
        this.adress = adress;
        this.userType = userType;
        this.grade = grade;
    }

    public Response() {
    }

    public String getAdress() {
        return adress;
    }

    public String getUserType() {
        return userType;
    }

    public int getGrade() {
        return grade;
    }
}
