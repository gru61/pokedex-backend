package pokedex.model.box;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pokedex.model.ownedpokemon.OwnedPokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


@Entity

public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    public long getId() {
        return id;
    }

    public BoxName getName() {
        return name;
    }

    public List<OwnedPokemon> getPokemons() {
        return Collections.unmodifiableList(pokemons);
    }

    public void addPokemon(OwnedPokemon pokemon) {
        if (pokemons.size() >= 20) {
            throw new IllegalArgumentException("Box ist schon voll");
        }
        pokemons.add(pokemon);
        pokemon.setBox(this);
    }

    public void removePokemon(OwnedPokemon pokemon) {
        pokemons.remove(pokemon);
        pokemon.setBox(null);
    }
}
