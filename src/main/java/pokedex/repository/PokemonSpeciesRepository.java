package pokedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pokedex.model.pokemonspecies.PokemonSpecies;

import java.util.Optional;

public interface PokemonSpeciesRepository extends JpaRepository<PokemonSpecies, Long> {
    Optional<PokemonSpecies> findByPokedexId(int pokedexId);
    Optional<PokemonSpecies> findByName(String name);
    boolean existsByPokedexId(int pokedexId);
    boolean existsByName(String name);
}
