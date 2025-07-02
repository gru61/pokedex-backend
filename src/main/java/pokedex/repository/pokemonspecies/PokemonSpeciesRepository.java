package pokedex.repository.pokemonspecies;

import org.springframework.data.jpa.repository.JpaRepository;
import pokedex.model.pokemonspecies.PokemonSpecies;

import java.util.List;
import java.util.Optional;


public interface PokemonSpeciesRepository extends JpaRepository<PokemonSpecies, Long> {
    Optional<PokemonSpecies> findByPokedexId(int pokedexId);
    List<PokemonSpecies> findByName(String name);
}
