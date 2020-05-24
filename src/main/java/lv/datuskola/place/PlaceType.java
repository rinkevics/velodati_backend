package lv.datuskola.place;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PlaceType {
    NARROW("Šaurība, nepārredzamība"),
    TIGHT_TURNS("Strauji pagriezieni"),
    PAVEMENT("Segums (bedres, augstas apmales)"),
    OTHER("Cits");

    public String label;

    PlaceType(String label) {
        this.label = label;
    }

    @JsonValue
    public int getJsonValue() {
        return ordinal();
    }
}
