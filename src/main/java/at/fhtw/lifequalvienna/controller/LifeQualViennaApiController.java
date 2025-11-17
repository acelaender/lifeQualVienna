package at.fhtw.lifequalvienna.controller;


import at.fhtw.lifequalvienna.model.Place;
import at.fhtw.lifequalvienna.model.Response;
import at.fhtw.lifequalvienna.model.Station;
import at.fhtw.lifequalvienna.service.AdressValidationService;
import at.fhtw.lifequalvienna.service.LifeQualViennaService;
import at.fhtw.lifequalvienna.service.OeffiStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080/"})
@RestController
@RequestMapping("/api/lifequalvienna")
public class LifeQualViennaApiController {
    private final LifeQualViennaService lifeQualViennaService;
    private final AdressValidationService adressValidationService;

    public LifeQualViennaApiController(LifeQualViennaService lifeQualViennaService, AdressValidationService adressValidationService) {
        this.lifeQualViennaService = lifeQualViennaService;
        this.adressValidationService = adressValidationService;
    }

    @GetMapping("/suggestion")
    public List<Place> getSuggestions(@RequestParam String adress) {
        return adressValidationService.findPlace(adress);
    }

    @GetMapping("/quality")
    public Response getQuality(@RequestParam String adress, @RequestParam double x, @RequestParam double y, @RequestParam String usertype) {
        Place place = new Place(adress, "", "", "", x, y);
        return lifeQualViennaService.calculateQual(place, usertype);
    }

}
