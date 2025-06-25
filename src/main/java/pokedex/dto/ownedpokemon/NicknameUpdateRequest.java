package pokedex.dto.ownedpokemon;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NicknameUpdateRequest {

    @Size(min = 1, max = 10)
    private String nickname;

}
