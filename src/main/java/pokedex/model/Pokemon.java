package pokedex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 1, message = "Was kommt vor der 1 ? (tipp: DU!)")
    @Max(value = 151, message = "Echte OG's wissen wie viele Pokemon es in der Gen 1 gibt (Ohne Missing No. Pokemon)")
    private int pokedexId;

    @NotBlank(message = "Das ist nicht das Haus von Schwarz und Weiss (setz einen Namen)")
    private String name;

    @NotBlank(message = "Ein Pokemon hat immer mindestens einen Typ")
    private String type1;

    private String type2;

    @Min(value = 1,message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private int level;

    @Min(value = 0, message = "Bei wem Schuldest du Pokemon?")
    private int amount;

    public Pokemon() {}

    public Pokemon(int pokedexId, String name, String type1, String type2, int level, int amount) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.level = level;
        this.amount = amount;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(int pokedexId) {
        this.pokedexId = pokedexId;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", pokedexId=" + pokedexId +
                ", name='" + name + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", level=" + level +
                ", amount=" + amount +
                '}';
    }
}
