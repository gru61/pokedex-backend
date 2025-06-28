package pokedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pokedex.model.box.Box;
import pokedex.model.ownedpokemon.OwnedPokemon;


public interface OwnedPokemonRepository extends JpaRepository<OwnedPokemon, Long> {

    @Query("select count(p) from OwnedPokemon p where p.box = :box")
    Long countByBox(@Param("box")Box box);
}
