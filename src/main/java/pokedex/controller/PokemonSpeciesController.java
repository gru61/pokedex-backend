package pokedex.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.PokemonSpecies;
import pokedex.repository.PokemonSpeciesRepository;

import java.util.List;

@RestController
@RequestMapping("/api/species")
public class PokemonSpeciesController {


    private final PokemonSpeciesRepository repository;

    @Autowired
    public PokemonSpeciesController(PokemonSpeciesRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public List<PokemonSpecies> getAllSpecies() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public PokemonSpecies getSpeciesById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Nicht Gefunden"));
    }

    @GetMapping("/byName/{name}")
    public PokemonSpecies getSpeciesByName(@PathVariable String name) {
        return repository.findAll()
                .stream()
                .filter(species -> species.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nicht Gefunden"));
    }
}
