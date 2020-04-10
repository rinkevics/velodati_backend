package lv.datuskola;

import lv.datuskola.place.Place;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Controller
public class AdminController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/admin")
    public String admin(Model model) {
//        List<Place> places = getPlaces();
//        model.addAttribute("places", places);
        return "admin";
    }
}
