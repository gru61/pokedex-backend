package pokedex.dto.ownedpokemon;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;


/**
 * DTO für das Aktualisieren eines bereits gefangenen Pokemon.
 * Wird für:
 * - Entwicklung
 * - Umbenennung (Nicknamen)
 * - Levelanpassung
 * genutzt
 */
@Setter
@Getter
@ToString
@Builder
public class UpdateOwnedRequest {


    /**
    * Die neue Species ID des Pokemon (für die Entwicklung)
    * Muss ein gültiger Pokedex-Eintrag sein und eine erlaubte Entwicklung darstellung
    */
    @NotNull(message = "Muss eine Pokedex ID haben")
    private Long speciesId;


    /**
    * Setzt den Spitznamen für das Pokemon.
    * Darf nicht leer sein, wenn man einen setzen will
    */
    @Size(min = 1, max = 10, message = "Nickname muss zwischen 1 und 10 Zeichen haben")
    private String nickname;

    /**
    * Hier wird das Level des Pokemon gesetzt.
    * Kann nicht kleiner sein als schon eingesetzt
    */
    @Min(value = 1, message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private Integer level;

    /**
     * Die Edition, in der sich das Pokemon sich befindet
     */
    @NotNull(message = "Edition darf nicht leer sein")
    private Edition edition;

    /**
     * Die Box, in der sich das Pokemon befindet
     */
    @NotNull(message = "Box muss angegeben werden")
    private BoxName box;
}