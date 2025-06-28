package pokedex.model.ownedpokemon;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.model.box.Box;
import pokedex.model.edition.Edition;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class OwnedPokemon {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private PokemonSpecies species;


    private String nickname;


    @Min(value = 1,message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private int level;


    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "box_id",  nullable = false)
    @JsonBackReference
    private Box box;


    @Enumerated(EnumType.STRING)
    @Column(name ="edition", nullable = false)
    private Edition edition;


    public OwnedPokemon(PokemonSpecies species, String nickname, int level, Edition edition, Box box) {
        this.species = species;
        this.nickname = nickname;
        this.level = level;
        this.edition = edition;
        this.box = box;
    }


    /**
     * Überprüft, ob zwei OwnedPokemon-Objekte gleich sind
     *
     * @param o Das zu vergleichende Objekt
     * @return true, wenn die IDs übereinstimmen
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnedPokemon other)) return false;
        return id != null && id.equals(other.id);
    }


    /**
     * Gibt den Hashcode basierend auf de rID zurück
     * @return Der Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    /**
     * Gibt eine lesbare String-Repräsentation des Objekts zurück
     * @return Eine formatierte String-Repräsentation
     */
    @Override
    public String toString() {
        return "OwnedPokemon{" +
                "id=" + id +
                ", species=" + species.getName() +
                ", nickname='" + nickname + '\'' +
                ", level=" + level +
                ", edition=" + edition +
                ", box=" + (box != null ? box.getName() : "null") +
                '}';
    }
}
