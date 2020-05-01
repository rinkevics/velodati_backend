package lv.datuskola;

import lv.datuskola.auth.PropertyProvider;
import lv.datuskola.place.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

import static lv.datuskola.MainApp.SERVER_URL;

@Controller("/")
public class AdminController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PropertyProvider propertyProvider;

    @GetMapping("/adminlogin")
    public String login() {
        return "adminlogin";
    }

    @PostMapping("/adminlogin")
    public String login(
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        if(password == null || !password.equals(propertyProvider.get("admin"))) {
            return "adminlogin";
        }

        Cookie cookie = new Cookie("admintoken", password);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return "redirect:" + propertyProvider.get(SERVER_URL) + "app/admin/new";
    }

    @GetMapping("/admin/{action}")
    public String admin(
            @CookieValue("admintoken") String adminToken,
            @PathVariable(value = "action", required = false) String action,
            Model model) {
        if(adminToken == null || !adminToken.equals(propertyProvider.get("admin"))) {
            return "adminlogin";
        }
        getPlaces(action, model);
        return "admin";
    }

    @PostMapping("/admin/{action}")
    @Transactional
    public String adminAction(
            @PathVariable("action") String action,
            @CookieValue("admintoken") String adminToken,
            @RequestParam(value = "approve", required = false) List<String> approveIds,
            @RequestParam(value = "block", required = false) List<String> blockIds,
            Model model
    ) {
        if(adminToken == null || !adminToken.equals(propertyProvider.get("admin"))) {
            return "adminlogin";
        }
        if(blockIds != null) {
            for(String id : blockIds) {
                Place place = entityManager.find(Place.class, Integer.valueOf(id));
                place.adminReviewed = true;
                place.blocked = true;
                entityManager.persist(place);
            }
        }
        if(approveIds != null) {
            for(String id : approveIds) {
                Place place = entityManager.find(Place.class, Integer.valueOf(id));
                place.adminReviewed = true;
                place.blocked = false;
                entityManager.persist(place);
            }
        }
        getPlaces(action, model);
        model.addAttribute("action", action);
        return "admin";
    }

    private void getPlaces(@PathVariable("action") String action, Model model) {
        if(action.equals("all")) {
            model.addAttribute("places", getAllPlaces());
        } else if(action.equals("new")) {
            model.addAttribute("places", getNewPlaces());
        }
    }

    private List<Place> getNewPlaces() {
        var query = entityManager.createQuery("SELECT p FROM Place p WHERE p.adminReviewed = false ORDER BY p.id DESC", Place.class);
        return query.getResultList();
    }

    private List<Place> getAllPlaces() {
        var query = entityManager.createQuery("SELECT p FROM Place p ORDER BY p.id DESC", Place.class);
        return query.getResultList();
    }
}
