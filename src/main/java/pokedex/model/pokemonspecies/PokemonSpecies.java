package pokedex.model.pokemonspecies;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PokemonSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Min(value = 1, message = "Was kommt vor der 1? (tipp:DU!)")
    @Max(value = 151, message = "Echte OG's wissen wie vile ePokemon es in der Gen 1 gibt")
    private int pokedexId;

    @NotBlank(message = "Das ist nicht das Haus von Schwarz und Weiss")
    @Column(unique = true)
    @Size(max=11)//längster Name der ersten Gen = Knuddeluff
    private String name;

    @NotBlank(message = "Ein Pokemon hat mindestens einen Typ")
    @Size(max=7)//längster Typ z.B. Elektro
    private String type1;

    private String type2;

    public PokemonSpecies() {}


    public PokemonSpecies(int pokedexId, String name, String type1, String type2) {
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
