package pokedex.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;


@Setter
@Getter
@ToString
public class CreateOwnedRequest {

    @NotNull(message = "Die Pokedex ID darf nicht NULL sein")
    private Long speciesId;

    @Min(value = 1, message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private Integer level;

    @NotNull(message = "Wähle eine Edition")
    private Edition edition;

    @NotNull(message = "Eine Box muss ausgewählt werden")
    private BoxName box;

    @Size(max = 10, message = "Nickname soll ja nicht länger als der Wahre Name sein")
    private String nickname;
}