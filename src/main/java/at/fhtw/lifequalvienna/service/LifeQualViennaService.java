package at.fhtw.lifequalvienna.service;


import at.fhtw.lifequalvienna.model.Response;
import org.springframework.stereotype.Service;

@Service
public class LifeQualViennaService {
    public Response calculateQual(String adress, String usertype) {
        return new Response(adress, usertype, 3);
    }

}
