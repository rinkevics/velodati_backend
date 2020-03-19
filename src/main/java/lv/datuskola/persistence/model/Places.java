package lv.datuskola.persistence.model;

import java.util.List;

public class Places {
    public final List<Place> places;
    public final List<Object[]> votes;

    public Places(List<Place> places, List<Object[]> votes) {
        this.places = places;
        this.votes = votes;
    }
}
