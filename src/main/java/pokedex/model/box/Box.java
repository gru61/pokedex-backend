package pokedex.model.box;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pokedex.model.ownedpokemon.OwnedPokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
* Diese Klasse soll als die Boxen und das Team repräsentieren, in denen die gefangenen Pokemon gespeichert werden.
* Enthält zusätzlich die Logik zur Validierung der Kapazitäten der einzelnen Boxen und des Teams
*/

@Entity
@Getter
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, updatable = false)
    @NotNull
    private BoxName name;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnedPokemon> pokemons = new ArrayList<>();

    public Box () {}

    public Box(BoxName name) {
        this.name = name;
    }

    @JsonManagedReference
    public List<OwnedPokemon> getPokemons() {
        return Collections.unmodifiableList(pokemons);
    }


    public void addPokemon(OwnedPokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon kann nicht null sein");
        }
        if (this.name == BoxName.TEAM && pokemons.size() >=6) {
            throw new IllegalArgumentException("Im Team dürfen nicht mehr als 6 Pokemon sein");
        }
        if (pokemons.size() >= 20) {
            throw new IllegalArgumentException("Box ist schon voll");
        }
        pokemons.add(pokemon);
        pokemon.setBox(this);
    }


    public void removePokemon(OwnedPokemon pokemon) {
        if (pokemon != null &&  pokemons.contains(pokemon)) {
        pokemons.remove(pokemon);
        pokemon.setBox(null);
        }
    }

    public static boolean isPokemonInAnyBox(OwnedPokemon pokemon, List<Box> allBoxes) {
        return allBoxes.stream()
                .anyMatch(box -> box.pokemons.contains(pokemon));
    }


    /**
     * Gibt die maximale Kapazität der ausgewählten Box zurück
     * @return 6 für TEAM und 20 für Box
     */
    public int getCapacity() {
        return switch (name) {
            case TEAM -> 6;
            default -> 20;
        };
    }

    /**
     * Gibt die aktuelle Anzahl an Pokemom in der Box zurück
     * @return Anzahl der Pokemon in dieser Box
     */
    public int getCurrentCount() {
        return pokemons.size();
    }
}
