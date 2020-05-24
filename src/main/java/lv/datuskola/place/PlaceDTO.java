package lv.datuskola.place;

public class PlaceDTO {

    public Integer id;
    public PlaceType placeType;
    public String lat;
    public String lon;
    public String img;
    public String description;

    public PlaceDTO(Integer id, PlaceType placeType, String lat, String lon, String img, String description) {
        this.id = id;
        this.placeType = placeType;
        this.lat = lat;
        this.lon = lon;
        this.img = img;
        this.description = description;
    }
}