package at.fhtw.lifequalvienna.controller;


import at.fhtw.lifequalvienna.model.Place;
import at.fhtw.lifequalvienna.model.Station;
import at.fhtw.lifequalvienna.service.AdressValidationService;
import at.fhtw.lifequalvienna.service.LifeQualViennaService;
import at.fhtw.lifequalvienna.service.OeffiStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/lifequalvienna")
public class LifeQualViennaApiController {
    private final LifeQualViennaService lifeQualViennaService;
    private final AdressValidationService adressValidationService;
    private final OeffiStationService qualityDataCollectionService;

    public LifeQualViennaApiController(LifeQualViennaService lifeQualViennaService, AdressValidationService adressValidationService, OeffiStationService qualityDataCollectionService) {
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

        return adressValidationService.findPlace(adress);
    }

}
