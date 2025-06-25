package pokedex.dto.ownedpokemon;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pokedex.model.edition.Edition;

@Setter
@Getter
public class EditionUpdateRequest {

    @NotNull(message = "Wähle eine Edition")
    private Edition edition;

}
