package pokedex.model.edition;

import lombok.Getter;

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

}
