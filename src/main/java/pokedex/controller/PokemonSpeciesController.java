package pokedex.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokedex.model.PokemonSpecies;
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

    @GetMapping("")
    public List<PokemonSpecies> getAllSpecies() {
        return speciesService.getAllSpecies();
    }

    @GetMapping("/{id}")
    public PokemonSpecies getSpeciesById(@PathVariable Long id) {
        return speciesService.getById(id);
    }

}
