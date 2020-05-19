package lv.datuskola.place;

public class PlaceData {
    public String img;
    public String description;
    public int placeType;
    public long voteCount;
    public String lat;
    public String lon;

    public PlaceData(String img, String description, int placeType, long voteCount, String lat, String lon) {
        this.img = img;
        this.description = description;
        this.placeType = placeType;
        this.voteCount = voteCount;
        this.lat = lat;
        this.lon = lon;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public int getPlaceType() {
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
