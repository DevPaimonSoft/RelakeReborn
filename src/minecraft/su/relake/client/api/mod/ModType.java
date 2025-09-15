package su.relake.client.api.mod;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ModType {
    COMBAT("Combat"),
    MOVE("Move"),
    RENDER("Render"),
    PLAYER("Player"),
    UTILS("Utils");

    String name;

    ModType(String name) {
        this.name = name;
    }

}
