package lv.datuskola.place;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lv.datuskola.vote.Vote;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer id;
    @Enumerated(EnumType.ORDINAL)
    public PlaceType placeType;
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
    public String  replyFromTownHall;
    @Enumerated(EnumType.ORDINAL)
    public TownHallReplyState townHallReplyState;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "place")
    public Set<Vote> votes;

    public Place() {

    }

    public Place(
            PlaceType placeType,
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

    public Integer getId() {
        return id;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public String getHash() {
        return hash;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getEmail() {
        return email;
    }

    public boolean isReceiveEmails() {
        return receiveEmails;
    }

    public boolean isAdminReviewed() {
        return adminReviewed;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public String getReplyFromTownHall() {
        return replyFromTownHall;
    }

    public TownHallReplyState getTownHallReplyState() {
        return townHallReplyState;
    }
}