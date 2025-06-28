package pokedex.dto;


import lombok.Data;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;
import pokedex.model.ownedpokemon.OwnedPokemon;


/**
 * DTO zu Darstellung eines gefangenen Pokemon im Frontend.
 * Enthält alle wichtigen Infos zur Anzeige
 */
@Data
public class OwnedPokemonDTO {
    private Long id;
    private String nickname;
    private int level;
    private Edition edition;
    private BoxName boxName;
    private Integer speciesId;
    private String speciesName;
    private String type1;
    private String type2;

    private OwnedPokemonDTO() {}

    /**
     * Erstellt ein DTO aus einem OwnedPokemon-Objekt
     * @param pokemon Das OwnedPokemon-Objekt
     * @return DTO für die API
     */
    public static OwnedPokemonDTO from(OwnedPokemon pokemon) {
        OwnedPokemonDTO dto = new OwnedPokemonDTO();
        dto.setId(pokemon.getId());
        dto.setNickname(pokemon.getNickname());
        dto.setLevel(pokemon.getLevel());
        dto.setEdition(pokemon.getEdition());
        dto.setBoxName(pokemon.getBox().getName());
        dto.setSpeciesId(pokemon.getSpecies().getPokedexId());
        dto.setSpeciesName(pokemon.getSpecies().getName());
        dto.setType1(pokemon.getSpecies().getType1().getDisplayName());
        dto.setType2(pokemon.getSpecies().getType2() !=null ? pokemon.getSpecies().getType2().getDisplayName() : null);

        return dto;
    }
}
