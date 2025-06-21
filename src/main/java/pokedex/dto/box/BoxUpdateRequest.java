package pokedex.dto.box;

import jakarta.validation.constraints.NotNull;
import pokedex.model.box.BoxName;


public class BoxUpdateRequest {

    @NotNull(message = "Box darf nicht leer sein")
    private BoxName boxName;

    public BoxName getBoxName() {
        return boxName;
    }

    public void setBoxName(BoxName boxName) {
        this.boxName = boxName;
    }
}
