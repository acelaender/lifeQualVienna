package at.fhtw.lifequalvienna.service;


import at.fhtw.lifequalvienna.model.Place;
import at.fhtw.lifequalvienna.model.Response;
import at.fhtw.lifequalvienna.model.Score;
import at.fhtw.lifequalvienna.model.UserType;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;

@Service
public class LifeQualViennaService {
    private AirQualityService airQualityService;
    private OeffiStationService oeffiStationService;
    private ParkService parkService;
    private PoliceStationService policeStationService;
    private AdressValidationService adressValidationService;
    private GeoService geoService;

    public LifeQualViennaService(AirQualityService airQualityService, OeffiStationService oeffiStationService, ParkService parkService, PoliceStationService policeStationService, AdressValidationService adressValidationService, GeoService geoService) {
        this.airQualityService = airQualityService;
        this.oeffiStationService = oeffiStationService;
        this.parkService = parkService;
        this.policeStationService = policeStationService;
        this.adressValidationService = adressValidationService;
        this.geoService = geoService;
    }

    public Response calculateQual(Place place, String usertype) {

        UserType user;

        switch (usertype){
            case "family":
                user = UserType.FAMILY;
                break;
            case "student":
                user = UserType.STUDENT;
                break;
            case "petowner":
                user = UserType.PETOWNER;
                break;
            default:
                user = UserType.STUDENT;
                break;
        }

        double totalScore = 0;
        String totalExpl = "";

        System.out.println(place.getX() + ", " + place.getY());
        //TODO
        System.out.println("place coords: " + place.getX() + ", " + place.getY());

        double airScore = airQualityService.getPollutionScore(place.getX(), place.getY());
        //TODO
        System.out.println("air score: " + airScore);
        totalScore += ( airScore * user.airQualityWeight );
        totalExpl += "The score for the air-quality is: " + airScore;

        Score oeffiScore = oeffiStationService.calculateScore(place.getX(), place.getY());
        //TODO
        System.out.println(oeffiScore.getExplanation());
        totalScore += ( oeffiScore.getScore() * user.transportWeight );
        totalExpl += oeffiScore.getExplanation();

        Score parkScore = parkService.calculateScore(place.getX(), place.getY());
        //TODO
        System.out.println(parkScore.getExplanation());
        totalScore += ( parkScore.getScore() * user.parksWeight );
        totalExpl += parkScore.getExplanation();

        Score policeScore = policeStationService.calculateScore(place.getX(), place.getY());
        //TODO
        System.out.println(policeScore.getExplanation());
        totalScore += ( policeScore.getScore() * user.policeWeight );
        totalExpl += policeScore.getExplanation();

        return new Response(place.getAdress(), usertype, totalScore, totalExpl);
    }

}
