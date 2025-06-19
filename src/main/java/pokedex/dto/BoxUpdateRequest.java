package pokedex.dto;

import jakarta.validation.constraints.NotBlank;


public class BoxUpdateRequest {

    @NotBlank(message = "Boxname darf nicht leer sein")
    private String boxName;

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }
}
