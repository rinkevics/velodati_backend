package lv.datuskola.place;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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

    @GetMapping(value="/places", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    public String listPlaces() throws JsonProcessingException {
        return "{ \"places\": " + places() +
                ", \"votes\": " + placeVotes() + "}";
    }

    @GetMapping(value="/data")
    @Transactional
    public String data() {
        return "data";
    }

    private String places() throws JsonProcessingException {
        List<Place> places = getPlaces();

        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(places);
    }

    private List<Place> getPlaces() {
        var query = entityManager.createQuery("SELECT p FROM Place p", Place.class);
        return query.getResultList();
    }

    private String placeVotes() {
        var query = entityManager.createQuery("SELECT v.place.id, count(v) FROM Vote v GROUP BY v.place.id");
        List<Object[]> results = query.getResultList();
        var jsonObjectBuilder = Json.createObjectBuilder();
        for(Object[] result : results) {
            jsonObjectBuilder.add(result[0].toString(), result[1].toString());
        }
        return jsonObjectBuilder.build().toString();
    }

}