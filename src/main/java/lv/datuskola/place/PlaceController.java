package lv.datuskola.place;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Controller
public class PlaceController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping(value = "/places", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    public String listPlaces() throws JsonProcessingException {
        return "{ \"places\": " + places() + ", \"votes\": " + placeVotes() + "}";
    }

    @GetMapping(value="/data")
    @Transactional
    public String data() {
        return "data";
    }

    @GetMapping(value = "/RZajXyYh")
    @Transactional
    public String data(Model model) {
        model.addAttribute("places", getPlaceData());
        return "data";
    }

    private String places() throws JsonProcessingException {
        List<Place> places = getPlaces();
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(places);
    }

    private List<PlaceData> getPlaceData() {
        var result = entityManager
                .createQuery(
                "SELECT new lv.datuskola.place.PlaceData(p.img, p.description, p.placeType, count(vote)) " +
                        "FROM Place p " +
                        "LEFT JOIN p.votes as vote " +
                        "WHERE p.blocked = FALSE " +
                        "GROUP BY p.img, p.description, p.placeType " +
                        "ORDER BY count(vote) DESC")
                .getResultList();
        return result;
    }

    private List<Place> getPlaces() {
        var query = entityManager.createQuery("SELECT p FROM Place p WHERE p.blocked = FALSE", Place.class);
        return query.getResultList();
    }

    private String placeVotes() {
        var query = entityManager.createQuery("SELECT v.place.id, count(v) FROM Vote v GROUP BY v.place.id");
        List<Object[]> results = query.getResultList();
        var jsonObjectBuilder = Json.createObjectBuilder();
        for (Object[] result : results) {
            jsonObjectBuilder.add(result[0].toString(), result[1].toString());
        }
        return jsonObjectBuilder.build().toString();
    }

}