package pokedex.dto;

import lombok.Builder;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;

import java.util.List;
import java.util.stream.Collectors;


/**
 * DTO zur Darstellung einer Box mit den enthaltenden Pokemon.
 * Wird genutzt, um Bax Daten an das UI zu übergeben
 */
@Builder
public class BoxDTO {
    private BoxName name;
    private int capacity;
    private List<OwnedPokemonDTO> pokemons;


    /**
     * Erstellt ein {@link BoxDTO}-Objekt aus einer {@link Box}-Entität.
     * Die Methode übernimmt die wichtigsten Eigenschaften der Box -Name, Kapazität und
     * enthaltente Pokemon - und mappt jedes enthaltene {@link pokedex.model.ownedpokemon.OwnedPokemon} via
     * {@link OwnedPokemonDTO#from(pokedex.model.ownedpokemon.OwnedPokemon)} ein ein DTO für die API-Ausgabe.
     * @param box Die zu konvertierende Box-Entität
     * @return Eine für das Frontend geeignete DTO-Repräsentation der Box
     */
    public static BoxDTO from (Box box) {
        return BoxDTO.builder()
                .name(box.getName())
                .capacity(box.getCapacity())
                .pokemons(box.getPokemons()
                        .stream()
                        .map(OwnedPokemonDTO::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
