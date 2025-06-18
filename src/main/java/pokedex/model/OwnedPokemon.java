package pokedex.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class OwnedPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PokemonSpecies species;

    private String nickname;

    @Min(value = 1,message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private int level;

    @ManyToOne(optional = false)
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

    public Long getId() {
        return id;
    }

    public PokemonSpecies getSpecies() {
        return species;
    }

    public void setSpecies(PokemonSpecies species) {
        this.species = species;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }
}
