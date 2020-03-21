package lv.datuskola.place;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer id;
    public Integer placeType;
    public String lat;
    public String lon;
    public String img;
    public String description;
    public LocalDateTime createdDateTime;
    public String hash;

    public Place() {

    }

    public Place(Integer placeType, String lat, String lon, String img, String description, LocalDateTime createdDateTime, String hash) {
        this.placeType = placeType;
        this.lat = lat;
        this.lon = lon;
        this.img = img;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.hash = hash;
    }
}