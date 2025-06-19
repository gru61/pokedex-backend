package pokedex.model;

public enum Edition {

    GELB("Gelb"),
    ROT("Rot"),
    BLAU("Blau"),
    GRÜN("Grün");

    private final String displayName;
    Edition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
