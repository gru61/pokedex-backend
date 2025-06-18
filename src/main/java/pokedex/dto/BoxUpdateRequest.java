package pokedex.dto;

import jakarta.validation.constraints.NotBlank;
import pokedex.model.Box;

public class BoxUpdateRequest {

    @NotBlank(message = "Boxname darf nicht leer sein")
    private String box;

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }
}
