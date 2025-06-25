package pokedex.dto.ownedpokemon;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;

@Getter
@Setter
public class OwnedPokemonRequest {

    @NotNull(message = "speciesId darf nicht NULL sein")
    private Long speciesId;

    private String nickname;

    @Min(value = 1, message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private int level;

    @NotNull
    private Edition edition;

    @NotNull(message = "Es muss eine Box ausgewählt sein")
    private BoxName box;

    public void setNickname(String nickname) {
        this.nickname = this.nickname;
    }

}
