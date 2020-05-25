package lv.datuskola.place;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TownHallReplyState {
    NONE("Nav"),
    REQUEST_SENT("Pieprasījums nosūtīts"),
    REPLY_RECEIVED("Atbilde saņemta");

    public String label;

    TownHallReplyState(String label) {
        this.label = label;
    }

    @JsonValue
    public int getJsonValue() {
        return ordinal();
    }

    public String getLabel() {
        return label;
    }
}
