package pokedex.dto.box;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pokedex.model.box.BoxName;


@Setter
@Getter
public class BoxUpdateRequest {

    @NotNull
    private BoxName boxName;

}
