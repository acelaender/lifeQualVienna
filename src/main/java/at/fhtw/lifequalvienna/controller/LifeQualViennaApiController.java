package at.fhtw.lifequalvienna.controller;


import at.fhtw.lifequalvienna.model.Response;
import at.fhtw.lifequalvienna.service.LifeQualViennaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
