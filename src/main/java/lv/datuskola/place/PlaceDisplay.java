package lv.datuskola.place;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lv.datuskola.vote.Vote;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

public class PlaceDisplay {

    public Integer id;
    public Integer placeType;
    public String lat;
    public String lon;
    public String img;
    public String description;

    public PlaceDisplay(Integer id, Integer placeType, String lat, String lon, String img, String description) {
        this.id = id;
        this.placeType = placeType;
        this.lat = lat;
        this.lon = lon;
        this.img = img;
        this.description = description;
    }
}