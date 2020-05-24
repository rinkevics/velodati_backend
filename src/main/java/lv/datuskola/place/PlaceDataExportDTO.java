package lv.datuskola.place;

public class PlaceDataExportDTO {
    public int id;
    public String img;
    public String description;
    public PlaceType placeType;
    public long voteCount;
    public String lat;
    public String lon;

    public PlaceDataExportDTO(int id, String img, String description, PlaceType placeType, long voteCount, String lat, String lon) {
        this.id = id;
        this.img = img;
        this.description = description;
        this.placeType = placeType;
        this.voteCount = voteCount;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
