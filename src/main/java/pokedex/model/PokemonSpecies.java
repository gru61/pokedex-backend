package pokedex.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class PokemonSpecies {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @Min(value = 1, message = "Was kommt vor der 1? (tipp:DU!)")
    @Max(value = 151, message = "Echte OG's wissen wie vile ePokemon es in der Gen 1 gibt")
    private int pokedexId;

    @NotBlank(message = "Das ist nicht das Haus von Schwarz und Weiss")
    @Column(unique = true)
    private String name;

    @NotBlank(message = "Ein Pokemon hat mindestens einen Typ")
    private String type1;

    private String type2;

    public PokemonSpecies() {}


    public PokemonSpecies(int pokedexId, String name, String type1, String type2) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(int pokedexId) {
        this.pokedexId = pokedexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
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
