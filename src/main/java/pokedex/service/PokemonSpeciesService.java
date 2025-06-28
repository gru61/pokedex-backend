package pokedex.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.repository.PokemonSpeciesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;


/**
 * Service zur Verwaltung von Pokemon-Arten
 */
@Service
public class PokemonSpeciesService {


    private static final Logger logger = LoggerFactory.getLogger(PokemonSpeciesService.class);


    private final PokemonSpeciesRepository speciesRepo;


    public PokemonSpeciesService(PokemonSpeciesRepository speciesRepo) {
        this.speciesRepo = speciesRepo;
    }


    /**
     * Gibt alle Pokemon-Arten zurück
     * @return Liste aller Pokemon-Arten
     */
    public List<PokemonSpecies> getAllSpecies() {
        logger.info("Alle Pokemon-Arten abgerufen");
        return speciesRepo.findAll();
    }


    /**
     * Gibt eine Pokemon-Art anhand dessen Pokedex-ID zurück
     * @param pokedexId die Pokedex-ID der gesuchten Art
     * @return Dei gefundene Pokemon-Art
     * @throws ResponseStatusException Wenn die Pokemon-Art nicht gefunden wurde
     */
    public PokemonSpecies getByPokedexId(int pokedexId) {
        logger.info("Pokemon-Art per Pokedex-ID: {} abgerufen", pokedexId);
        return speciesRepo.findByPokedexId(pokedexId)
                .orElseThrow(()-> {
                    logger.warn("Pokemon-Art mit der Pokedex-ID {} nicht gefunden", pokedexId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden");
                });
    }


    /**
     * Gibt eine Pokemon-Art anhand dessen Namens zurück.
     * @param name Der Name der gesuchten Art
     * @return Die gefundenen Pokemon-Art
     * @throws ResponseStatusException Wenn die Art nicht gefunden wurde oder mehrere Ergebnisse vorliegen
     */
    public List<PokemonSpecies> getByName(String name) {
        logger.info("Suche nach Pokemon-Art per Namen: {} abgerufen", name);
        List<PokemonSpecies> result = speciesRepo.findByName(name);

        if (result.isEmpty()) {
            logger.warn("Keine Pokemon-Art mit diesem Namen {} gefunden", name);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon: " +name + " nicht gefunden" );
        }

        logger.info("Pokemon-Art mit Namen {} gefunden", name);
        return result;
    }
}
