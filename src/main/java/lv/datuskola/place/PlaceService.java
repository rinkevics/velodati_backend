package lv.datuskola.place;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class PlaceService {

    @PersistenceContext
    private EntityManager entityManager;

    String places() throws JsonProcessingException {
        List<PlaceDTO> places = getPlaces();
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(places);
    }

    public List<PlaceDataExportDTO> getPlaceData() {
        var result = entityManager.createQuery(
                """
                SELECT new lv.datuskola.place.PlaceDataExportDTO(p.id, p.img, p.description, p.placeType, count(vote), p.lat, p.lon, p.replyFromTownHall, p.townHallReplyState)
                FROM Place p
                LEFT JOIN p.votes as vote
                WHERE p.blocked = FALSE
                GROUP BY p.id, p.img, p.description, p.placeType, p.lat, p.lon
                ORDER BY count(vote) DESC
                """,
                PlaceDataExportDTO.class)
                .getResultList();
        return result;
    }

    List<PlaceDTO> getPlaces() {
        var result = entityManager.createQuery(
                """
                SELECT new lv.datuskola.place.PlaceDTO(p.id, p.placeType, p.lat, p.lon, p.img, p.description)
                FROM Place p 
                WHERE p.blocked = FALSE
                """,
                PlaceDTO.class)
                .getResultList();
        return result;
    }

    String placeVotes() {
        var query = entityManager.createQuery("SELECT v.place.id, count(v) FROM Vote v GROUP BY v.place.id");
        List<Object[]> results = query.getResultList();
        var jsonObjectBuilder = Json.createObjectBuilder();
        for (Object[] result : results) {
            jsonObjectBuilder.add(result[0].toString(), result[1].toString());
        }
        return jsonObjectBuilder.build().toString();
    }
}
