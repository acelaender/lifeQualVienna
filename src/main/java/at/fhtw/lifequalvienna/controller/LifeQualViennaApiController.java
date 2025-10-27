package at.fhtw.lifequalvienna.controller;


import at.fhtw.lifequalvienna.model.Response;
import at.fhtw.lifequalvienna.service.LifeQualViennaService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/lifequalvienna")
public class LifeQualViennaApiController {
    private final LifeQualViennaService lifeQualViennaService;

    public LifeQualViennaApiController(LifeQualViennaService lifeQualViennaService) {
        this.lifeQualViennaService = lifeQualViennaService;
    }

    @GetMapping
    public Response getLifeQual(
            @RequestParam String adress,
            @RequestParam String userType
    ) {
        return lifeQualViennaService.calculateQual(adress, userType);
    }
}
