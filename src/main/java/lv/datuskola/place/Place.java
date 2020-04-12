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
    public String ipAddress;
    public String email;
    public boolean receiveEmails;
    public boolean adminReviewed;
    public boolean blocked;

    public Place() {

    }

    public Place(
            Integer placeType,
            String lat,
            String lon,
            String img,
            String description,
            LocalDateTime createdDateTime,
            String hash,
            String ipAddress,
            String email,
            boolean receiveEmails,
            boolean adminReviewed,
            boolean blocked) {
        this.placeType = placeType;
        this.lat = lat;
        this.lon = lon;
        this.img = img;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.hash = hash;
        this.ipAddress = ipAddress;
        this.email = email;
        this.receiveEmails = receiveEmails;
        this.adminReviewed = adminReviewed;
        this.blocked = blocked;
    }
}