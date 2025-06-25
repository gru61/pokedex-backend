package pokedex.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.repository.PokemonSpeciesRepository;

import java.util.List;

@Service
public class PokemonSpeciesService {


    private final PokemonSpeciesRepository speciesRepo;

    public PokemonSpeciesService(PokemonSpeciesRepository speciesRepo) {
        this.speciesRepo = speciesRepo;
    }

    public List<PokemonSpecies> getAllSpecies() {
        return speciesRepo.findAll();
    }

    public PokemonSpecies getById(long id) {
        return speciesRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));
    }

    public PokemonSpecies getByPokedexId(int pokedexId) {
        return speciesRepo.findByPokedexId(pokedexId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));
    }

    public List<PokemonSpecies> getByName(String name) {
        List<PokemonSpecies> result = speciesRepo.findByName(name);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon: " +name + " nicht gefunden" );
        }
        return result;
    }
}
