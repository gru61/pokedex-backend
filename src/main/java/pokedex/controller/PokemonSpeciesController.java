package pokedex.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.service.PokemonSpeciesService;

import java.util.List;

@RestController
@RequestMapping("/api/species")
public class PokemonSpeciesController {


    private final PokemonSpeciesService speciesService;

    @Autowired
    public PokemonSpeciesController(PokemonSpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @Operation(summary = "Gibt eine Liste aller 151 Pokemon zurück", description = "Wird für die UI des Pokedex gebraucht")
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen")
    @GetMapping("")
    public List<PokemonSpecies> getAllSpecies() {
        return speciesService.getAllSpecies();
    }

    @Operation(summary = "Sucht das Ziel Pokemon anhand des ID", description = "Nur für interne zwecke")
    @ApiResponse(responseCode = "200", description = "Pokemon wurde gefunden")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @GetMapping("/{id}")
    public PokemonSpecies getSpeciesById(@PathVariable Long id) {
        return speciesService.getById(id);
    }

    @Operation(summary = "Sucht das Ziel Pokemon anhand des Pokedex ID")
    @ApiResponse(responseCode = "200", description = "Pokemon wurde gefunden")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @GetMapping("/pokedex-id/{pokedexId}")
    public PokemonSpecies getByPokedexId(@PathVariable int pokedexId) {
        return speciesService.getByPokedexId(pokedexId);
    }

    @Operation(summary = "Sucht ein Pokemon nach Namen", description = "Gibt eine Liste aller Treffer zurück")
    @ApiResponse(responseCode = "200", description = "Pokemon wurde gefunden")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @GetMapping("/name/{name}")
    public List<PokemonSpecies> getByName(@PathVariable String name) {
        return speciesService.getByName(name);
    }

}
