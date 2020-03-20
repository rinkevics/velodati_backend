package lv.datuskola.persistence.model;

import lv.datuskola.file.Place;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer id;

    @ManyToOne
    @JoinColumn(name="place_id", nullable=false)
    public Place place;

    public LocalDateTime createdDateTime;
    public String userHash;
    public boolean isBlocked;

    public Vote() {
    }

    public Vote(Place place, LocalDateTime createdDateTime, String userHash) {
        this.place = place;
        this.createdDateTime = createdDateTime;
        this.userHash = userHash;
    }
}
