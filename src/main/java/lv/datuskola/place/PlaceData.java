package lv.datuskola.place;

public class PlaceData {
    public String img;
    public String description;
    public int placeType;
    public long voteCount;

    public PlaceData(String img, String description, int placeType, long voteCount) {
        this.img = img;
        this.description = description;
        this.placeType = placeType;
        this.voteCount = voteCount;
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
}
