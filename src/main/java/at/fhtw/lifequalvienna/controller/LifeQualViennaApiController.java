package at.fhtw.lifequalvienna.controller;


import at.fhtw.lifequalvienna.model.Place;
import at.fhtw.lifequalvienna.model.Response;
import at.fhtw.lifequalvienna.model.Station;
import at.fhtw.lifequalvienna.service.AdressValidationService;
import at.fhtw.lifequalvienna.service.LifeQualViennaService;
import at.fhtw.lifequalvienna.service.QualityDataCollectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/lifequalvienna")
public class LifeQualViennaApiController {
    private final LifeQualViennaService lifeQualViennaService;
    private final AdressValidationService adressValidationService;
    private final QualityDataCollectionService qualityDataCollectionService;

    public LifeQualViennaApiController(LifeQualViennaService lifeQualViennaService, AdressValidationService adressValidationService, QualityDataCollectionService qualityDataCollectionService) {
        this.lifeQualViennaService = lifeQualViennaService;
        this.adressValidationService = adressValidationService;
        this.qualityDataCollectionService = qualityDataCollectionService;
    }

    /*
    @GetMapping
    public Response getLifeQual(
            @RequestParam String adress,
            @RequestParam String userType
    ) {
        return lifeQualViennaService.calculateQual(adress, userType);
    }
    */
    @GetMapping("/suggestion")
    public List<Place> getSuggestions(@RequestParam String adress) {
        List<Place> placesTmp = adressValidationService.findPlace(adress);

        System.out.println(placesTmp.get(0).getAdress());

        Station station = qualityDataCollectionService.findNearest(placesTmp.get(0).getX(), placesTmp.get(0).getY());
        System.out.println(station.getName());
        return adressValidationService.findPlace(adress);
    }

}
