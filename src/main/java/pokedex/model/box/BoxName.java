package pokedex.model.box;


/**
* Enum für die Darstellung der verschiedenen Boxen im System.
* Enthält auch lesbare Namen für die Anzeige im Frontend.
* TEAM Slot >1 && <=6
* BOX.... >0 && <=20
*/

public enum BoxName {
    TEAM("Team"),
    BOX1("Box 1"),
    BOX2("Box 2"),
    BOX3("Box 3"),
    BOX4("Box 4"),
    BOX5("Box 5"),
    BOX6("Box 6"),
    BOX7("Box 7"),
    BOX8("Box 8"),
    BOX9("Box 9"),
    BOX10("Box 10"),
    BOX11("Box 11"),
    BOX12("Box 12");

    /*
    * Für die UI Anzeige im Frontend
    * */
    private final String displayName;

    BoxName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}


