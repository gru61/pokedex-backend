package pokedex.model.type;


/*
*
* */
public enum PokemonType {
    NORMAL("Normal"),
    PFLANZE("Pflanze"),
    GIFT("Gift"),
    FEUER("Feuer"),
    FLUG("Flug"),
    WASSER("Wasser"),
    KÄFER("Käfer"),
    ELEKTRO("Elektro"),
    BODEN("Boden"),
    KAMPF("Kampf"),
    PSYCHO("Psycho"),
    GESTEIN("Gestein"),
    EIS("Eis"),
    GEIST("Geist"),
    DRACHE("Drache");

    private final String displayName;

    PokemonType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}