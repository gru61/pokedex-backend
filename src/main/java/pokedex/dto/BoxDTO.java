package pokedex.dto;

import lombok.Data;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoxDTO {
    private BoxName name;
    private int capacity;
    private List<OwnedPokemonDTO> pokemons;

    public static BoxDTO from(Box box) {
        BoxDTO dto = new BoxDTO();
        dto.setName(box.getName());
        dto.setCapacity(box.getCapacity());
        dto.setPokemons(box.getPokemons().stream()
                .map(OwnedPokemonDTO::from)
                .collect(Collectors.toList())
        );
        return dto;
    }
}
