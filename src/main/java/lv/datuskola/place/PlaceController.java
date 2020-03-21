package lv.datuskola.place;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Places listPlaces() {
        var result = new Places(getPlaces(), getPlaceVotes());
        return result;
    }

    private List<Place> getPlaces() {
        var query = entityManager.createQuery("SELECT p FROM Place p", Place.class);
        return query.getResultList();
    }

    private List<Object[]> getPlaceVotes() {
        var query = entityManager.createQuery("SELECT v.place.id, count(v) FROM Vote v GROUP BY v.place.id");
        List<Object[]> results = query.getResultList();
        return results;
    }

}