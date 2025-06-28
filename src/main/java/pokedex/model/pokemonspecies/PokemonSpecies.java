package pokedex.model.pokemonspecies;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pokedex.model.type.PokemonType;

@Setter
@Getter
@Entity
public class PokemonSpecies {

    private static final int MAX_NAME_LENGTH = 11;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Min(value = 1, message = "Was kommt vor der 1? (tipp:DU!)")
    @Max(value = 151, message = "Echte OG's wissen wie vile ePokemon es in der Gen 1 gibt")
    private int pokedexId;

    @NotBlank(message = "Das ist nicht das Haus von Schwarz und Weiss")
    @Column(unique = true)
    @Size(max=MAX_NAME_LENGTH)//längster Name der ersten Gen = Knuddeluff
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type1", nullable = false)
    private PokemonType type1;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type2")
    private PokemonType type2;

    public PokemonSpecies() {}


    public PokemonSpecies(int pokedexId, String name, PokemonType type1, PokemonType type2) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "PokemonSpecies{" +
                "id=" + id +
                ", PokedexId=" + pokedexId +
                ", Name='" + name + '\'' +
                ", Type1='" + type1 + '\'' +
                ", Type2='" + type2 + '\'' +
                '}';
    }
}
