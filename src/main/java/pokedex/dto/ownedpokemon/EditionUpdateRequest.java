package pokedex.dto.ownedpokemon;

import pokedex.model.edition.Edition;

public class EditionUpdateRequest {

    private Edition edition;

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }
}
