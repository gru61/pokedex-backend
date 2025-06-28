package pokedex.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.service.PokemonSpeciesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Controller zur Verwaltung aller Pokemon-Arten über die REST-API.
 */
@RestController
@RequestMapping("/api/species")
public class PokemonSpeciesController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonSpeciesController.class);
    private final PokemonSpeciesService speciesService;


    public PokemonSpeciesController(PokemonSpeciesService speciesService) {
        this.speciesService = speciesService;
    }


    /**
     * Gibt alle Pokemon-Arten zurück
     * @return Eine Liste der Pokemon-Arten
     */
    @Operation(summary = "Gibt eine Liste aller 151 Pokemon zurück", description = "Wird für die UI des Pokedex gebraucht")
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen")
    @GetMapping
    public List<PokemonSpecies> getAllSpecies() {
        logger.info("Rufe alle Pokemon-Arten auf");
        return speciesService.getAllSpecies();
    }


    /**
     * Sucht eine Pokemon-Art anhand seiner Pokedex-ID.
      * @param pokedexId Die Arten-ID des zu suchenden Pokemon-Art
     * @return Das gefundene Pokemon
     */
    @Operation(summary = "Sucht das Ziel Pokemon anhand des Pokedex ID")
    @ApiResponse(responseCode = "200", description = "Pokemon wurde gefunden")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @GetMapping("/pokedex-id/{pokedexId}")
    public PokemonSpecies getByPokedexId(@PathVariable int pokedexId) {
        logger.info("Ruft die Pokemon-Art anhand des Pokedex-ID {} auf", pokedexId);
        return speciesService.getByPokedexId(pokedexId);
    }


    /**
     * Sucht eine Pokemon-Art anhand dessen Namens.
     * @param name Den Namen der Pokemon-Art
     * @return Die gefundenen Pokemon-Art
     */
    @Operation(summary = "Sucht ein Pokemon nach Namen", description = "Gibt eine Liste aller Treffer zurück")
    @ApiResponse(responseCode = "200", description = "Pokemon wurde gefunden")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @GetMapping("/name/{name}")
    public List<PokemonSpecies> getByName(@PathVariable String name) {
        logger.info("Ruft die Pokemon-Art per Namen {} auf", name);
        return speciesService.getByName(name);
    }
}
