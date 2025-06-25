package pokedex.model.ownedpokemon;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.model.box.Box;
import pokedex.model.edition.Edition;

@Getter
@Setter
@Entity
public class OwnedPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private PokemonSpecies species;

    private String nickname;

    @Min(value = 1,message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private int level;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(nullable = false)
    private Box box;

    @Enumerated(EnumType.STRING)
    private Edition edition;

    public OwnedPokemon() {}

    public OwnedPokemon(PokemonSpecies species, String nickname, int level, Edition edition, Box box) {
        this.species = species;
        this.nickname = nickname;
        this.level = level;
        this.edition = edition;
        this.box = box;
    }

}
