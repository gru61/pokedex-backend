package pokedex.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.ownedpokemon.OwnedPokemon;
import pokedex.repository.BoxRepository;
import pokedex.repository.OwnedPokemonRepository;

import java.util.List;

@Service
public class BoxService {

    private final BoxRepository boxRepo;
    private final OwnedPokemonRepository ownedRepo;

    public BoxService(BoxRepository boxRepo, OwnedPokemonRepository ownedRepo) {
        this.boxRepo = boxRepo;
        this.ownedRepo = ownedRepo;
    }

    public List<Box> getAllBoxes() {
        return boxRepo.findAll();
    }

    public Box getBoxByName(BoxName name) {
        return boxRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));
    }

    public void checkCapacity(BoxName name) {
        if (boxRepo.countByName(name) >= 20) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Box ist schon voll");
        }
    }

    public boolean isFull(BoxName name) {
        return boxRepo.countByName(name) >= 20;
    }

    @Transactional
    public void movePokemon(BoxName sourceBox, BoxName targetBox, Long pokemonId) {
        if (sourceBox.equals(targetBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Boxen sind identisch");
        }

        OwnedPokemon pokemon = ownedRepo.findById(pokemonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));


        Box source = getBoxByName(sourceBox);
        Box target = getBoxByName(targetBox);

        if (!pokemon.getBox().getName().equals(sourceBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pokemon befindet sich nicht in dieser Box");
        }

        if (isFull(targetBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ziel Box ist schon voll");
        }

        pokemon.setBox(target);
        ownedRepo.save(pokemon);
    }

}
