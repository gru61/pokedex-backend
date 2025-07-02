package pokedex.model.edition;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;


/**
 *
 */
@Getter
public enum Edition {

    GELB("Gelb"),
    ROT("Rot"),
    BLAU("Blau"),
    GRÜN("Grün");

    private final String displayName;
    Edition(String displayName) {
        this.displayName = displayName;
    }

    public static List<String> getAllEditionNames() {
        return Arrays.stream(values())
                .map(Edition::getDisplayName)
                .toList();
    }
}
