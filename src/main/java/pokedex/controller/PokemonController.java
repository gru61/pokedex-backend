package pokedex.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.Pokemon;
import pokedex.repository.PokemonRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonController(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping("")
    public List<Pokemon> getAllPokemons() {
        return pokemonRepository.findAll();
    }

    @PostMapping("")
    public Pokemon createPokemon(@Valid @RequestBody Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    @GetMapping("/{id}")
    public Pokemon getPokemonById(@PathVariable long id) {
        return pokemonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));
    }

    @DeleteMapping("/{id}")
    public void deletePokemonById(@PathVariable long id) {
        pokemonRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Pokemon updatePokemon(@PathVariable long id, @Valid @RequestBody Pokemon pokemon) {
        if (pokemonRepository.existsById(id)) {
            pokemon.setId(id);
            return pokemonRepository.save(pokemon);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden");
        }
    }
}
