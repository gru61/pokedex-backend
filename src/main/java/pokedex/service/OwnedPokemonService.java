package pokedex.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.dto.box.BoxUpdateRequest;
import pokedex.dto.ownedpokemon.EditionUpdateRequest;
import pokedex.dto.ownedpokemon.LevelUpdateRequest;
import pokedex.dto.ownedpokemon.NicknameUpdateRequest;
import pokedex.dto.ownedpokemon.OwnedPokemonRequest;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;
import pokedex.model.ownedpokemon.OwnedPokemon;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.repository.BoxRepository;
import pokedex.repository.OwnedPokemonRepository;
import pokedex.repository.PokemonSpeciesRepository;

import java.util.List;
import java.util.Objects;


@Service
public class OwnedPokemonService {

    private final OwnedPokemonRepository ownedRepo;
    private final PokemonSpeciesRepository speciesRepo;
    private final BoxRepository boxRepo;

    public OwnedPokemonService(OwnedPokemonRepository ownedRepo,  PokemonSpeciesRepository speciesRepo,  BoxRepository boxRepo) {
        this.ownedRepo = ownedRepo;
        this.speciesRepo = speciesRepo;
        this.boxRepo = boxRepo;
    }

    public List<OwnedPokemon> getAllPokemon() {
        return ownedRepo.findAll();
    }

    public OwnedPokemon getPokemonById(long id) {
        return ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));
    }

    public OwnedPokemon addPokemon(OwnedPokemonRequest request) {
        PokemonSpecies species = speciesRepo.findById(request.getSpeciesId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));


        BoxName boxName = request.getBox();
        Box box = boxRepo.findByName(boxName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));

        checkBoxCapacity(boxName);

        OwnedPokemon pokemon = new OwnedPokemon(
                species,
                request.getNickname(),
                request.getLevel(),
                request.getEdition(),
                box
        );

        return ownedRepo.save(pokemon);
    }

    private void checkBoxCapacity(BoxName boxName) {
        long count = ownedRepo.countByBox_Name(boxName);
        if (count >= 20) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Die Box ist schon voll");
        }
    }

    public OwnedPokemon updatePokemon(long id, OwnedPokemonRequest request) {
        OwnedPokemon existing = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level kann nicht gesenkt werden");
        }

        PokemonSpecies species = speciesRepo.findById(request.getSpeciesId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültiger speciesId"));

        BoxName newBoxName = request.getBox();
        BoxName currentBoxName = existing.getBox() != null ? existing.getBox().getName() : null;

        if (!Objects.equals(newBoxName, currentBoxName)) {
            checkBoxCapacity(newBoxName);
            Box newBox = boxRepo.findByName(newBoxName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box nicht gefunden"));
            existing.setBox(newBox);
        }

        existing.setSpecies(species);
        existing.setNickname(request.getNickname());
        existing.setLevel(request.getLevel());
        existing.setEdition(request.getEdition());

        return ownedRepo.save(existing);
    }

    public OwnedPokemon updateLevel(long id, LevelUpdateRequest request) {
        OwnedPokemon existing = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dein Pokemon kann nicht schwächer werden");
        }

        existing.setLevel(request.getLevel());
        return ownedRepo.save(existing);
    }

    public String updateNickname(long id, NicknameUpdateRequest request) {
        OwnedPokemon pokemon = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        String newNickname = request.getNickname() == null ? null : request.getNickname().trim();
        if (newNickname != null && newNickname.isEmpty()) {
            newNickname = null;
        }

        String speciesName = pokemon.getSpecies().getName();
        if (newNickname != null && newNickname.equalsIgnoreCase(speciesName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Versuchst du deinem Pokemon den selben Namen zu geben wie er schon hat?, schwach");
        }

        pokemon.setNickname(newNickname);
        ownedRepo.save(pokemon);

        long count = ownedRepo.countByNicknameAndIdNot(newNickname, id);
        if (newNickname != null && count >=1) {
            return "Spitzname gegeben. Aber gehen dir die Ideen aus .....";
        }

        return "Spitzname gegeben";
    }

    public String updateBox(long id, BoxUpdateRequest request) {
        OwnedPokemon pokemon = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        BoxName newBoxName = request.getBoxName();
        BoxName currentBoxName = pokemon.getBox() != null ? pokemon.getBox().getName() : null;

        if (!Objects.equals(newBoxName, currentBoxName)) {
            Box newBox = boxRepo.findByName(newBoxName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box nicht gefunden"));

            if (ownedRepo.countByBox_Name(newBoxName) >= 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die Box ist bereits voll");
            }

            pokemon.setBox(newBox);
            ownedRepo.save(pokemon);
        }

        return "Box geändert auf: " + newBoxName;
    }

    public String updateEdition(long id, EditionUpdateRequest request) {
        OwnedPokemon pokemon = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        Edition edition = request.getEdition();
        if (edition == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Edition darf nicht leer sein");
        }

        pokemon.setEdition(edition);
        ownedRepo.save(pokemon);

        return "Edition auf: " + edition + " geändert";
    }

    public List<OwnedPokemon> getByEdition(Edition edition) {
        return ownedRepo.findByEdition(edition);
    }

    public void deletePokemonById(long id) {
        ownedRepo.deleteById(id);
    }

}
