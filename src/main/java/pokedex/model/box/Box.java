package pokedex.model.box;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Box {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, updatable = false)
    private BoxName name;


    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final List<OwnedPokemon> pokemons = new ArrayList<>();


    public Box(BoxName name) {
        this.name = name;
    }


    public List<OwnedPokemon> getPokemons() {
        return Collections.unmodifiableList(pokemons);
    }


    /**
     * Gibt die maximale Kapazität der ausgewählten Box zurück
     * @return 6 für TEAM sonst 20 für Box
     */
    public int getCapacity() {
        return name == BoxName.TEAM ? 6 : 20;

    }
}
