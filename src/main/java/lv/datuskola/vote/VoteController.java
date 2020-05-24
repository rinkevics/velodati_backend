package lv.datuskola.vote;

import lv.datuskola.place.Place;
import lv.datuskola.place.PlaceType;
import lv.datuskola.services.FacebookService;
import lv.datuskola.services.Recaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VoteController {

    @Autowired
    public FacebookService service;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping(value="/vote", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    public String vote(
            @RequestParam("place") int placeID,
            @RequestParam("isUpvote") boolean doUpvote,
            @CookieValue("token") String token,
            @RequestHeader(value = "x-captcha") String captcha
            ) throws IOException {

        if(!Recaptcha.isGoodCaptcha(captcha)) {
            return "blank";
        }

        String userId = service.isValid(token);
        if(userId == null) {
            return getResult(0L);
        }
        Place place = entityManager.find(Place.class, placeID);
        if(place == null) {
            return getResult(0L);
        }

        Long count = entityManager
                .createQuery("SELECT count(v) FROM Vote v WHERE v.place.id = :placeId AND v.userHash = :fbUser", Long.class)
                .setParameter("placeId", place.id)
                .setParameter("fbUser", userId)
                .getSingleResult();


        if(count > 0 && doUpvote) {
            Long voteCount = getVoteCount(place);
            return getResult(voteCount);
        } else if(count == 0 && !doUpvote) {
            Long voteCount = getVoteCount(place);
            return getResult(voteCount);
        }

        if(doUpvote) {
            Vote vote = new Vote(place, LocalDateTime.now(), userId);
            entityManager.persist(vote);
        } else {
            entityManager
                    .createQuery("DELETE FROM Vote v WHERE v.place.id = :placeId AND v.userHash = :fbUser")
                    .setParameter("placeId", place.id)
                    .setParameter("fbUser", userId)
                    .executeUpdate();
        }

        Long voteCount = getVoteCount(place);
        return getResult(voteCount);
    }

    @GetMapping(value="/top", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    public List top() {
        var result = new ArrayList();
        for(PlaceType placeType : PlaceType.values()) {
            getResult(result, placeType);
        }
        return result;
    }

    private void getResult(List top, PlaceType placeType) {
        List list = entityManager
                .createQuery(
                        """
                        SELECT v.place.id, count(v)
                        FROM Vote v
                        WHERE v.place.placeType = :type
                        AND LENGTH(v.place.replyFromTownHall) IS NULL
                        GROUP BY v.place.id
                        ORDER BY count(v) DESC
                        """)
                .setParameter("type", placeType)
                .getResultList();
        top.add(list.subList(0, Math.min(list.size(), 3)));
    }

    private Long getVoteCount(Place place) {
        return entityManager
                .createQuery("SELECT count(v) FROM Vote v WHERE v.place.id = :placeId", Long.class)
                .setParameter("placeId", place.id)
                .getSingleResult();
    }

    private String getResult(Long count) {
        return Json.createObjectBuilder()
                .add("votes", count)
                .build()
                .toString();
    }

    @GetMapping(value="/myvotes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String myVotes(@CookieValue(value = "token") String token) {
        String userId = service.isValid(token);
        if(userId == null) {
            return "blank";
        }

        Query query = entityManager.createQuery("SELECT v.place.id FROM Vote v WHERE v.userHash = :fbUser");
        query.setParameter("fbUser", userId);

        List<Integer> results = query.getResultList();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for(Integer result : results) {
            jsonObjectBuilder.add(result.toString(), true);
        }
        return jsonObjectBuilder.build().toString();
    }

}
