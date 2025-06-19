package pokedex.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pokedex.dto.*;
import pokedex.model.Box;
import pokedex.model.Edition;
import pokedex.model.OwnedPokemon;
import pokedex.model.PokemonSpecies;
import pokedex.repository.BoxRepository;
import pokedex.repository.OwnedPokemonRepository;
import pokedex.repository.PokemonSpeciesRepository;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/pokemon")
public class OwnedPokemonController {

    private final OwnedPokemonRepository ownedRepo;
    private final PokemonSpeciesRepository speciesRepo;
    private final BoxRepository boxRepo;

    @Autowired
    public OwnedPokemonController(OwnedPokemonRepository ownedRepo, PokemonSpeciesRepository speciesRepo, BoxRepository boxRepo) {
        this.ownedRepo = ownedRepo;
        this.speciesRepo = speciesRepo;
        this.boxRepo = boxRepo;
    }

    @GetMapping("")
    public List<OwnedPokemon> getAllPokemon() {
        return ownedRepo.findAll();
    }

    @PostMapping("")
    public OwnedPokemon createPokemon(@Valid @RequestBody OwnedPokemonRequest request) {
        PokemonSpecies species = speciesRepo.findById(request.getSpeciesId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültige speciesId"));

        Box box = boxRepo.findByName(request.getBox())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box nicht gefunden"));

        checkBoxCapacity(request.getBox());

        OwnedPokemon pokemon = new OwnedPokemon(
                species,
                request.getNickname(),
                request.getLevel(),
                request.getEdition(),
                box
        );
        return ownedRepo.save(pokemon);
    }

    @GetMapping("/{id}")
    public OwnedPokemon getPokemonById(@PathVariable long id) {
        return ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));
    }

    @DeleteMapping("/{id}")
    public void deletePokemonById(@PathVariable long id) {
        ownedRepo.deleteById(id);
    }

    @PutMapping("/{id}")
    public OwnedPokemon updatePokemon(@PathVariable long id, @Valid @RequestBody OwnedPokemonRequest request) {
        OwnedPokemon existing = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level kann nicht gesenkt werden");
        }

        PokemonSpecies species = speciesRepo.findById(request.getSpeciesId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültiger speciesId"));

        String newBoxName = request.getBox();
        String currentBoxName = existing.getBox() != null ? existing.getBox().getName() : null;

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

    @PatchMapping("/{id}/level")
    public OwnedPokemon updateLevel(@PathVariable long id, @Valid @RequestBody LevelUpdateRequest request) {
        OwnedPokemon existing = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dein Pokemon kann nicht schwächer werden");
        }

        existing.setLevel(request.getLevel());
        return ownedRepo.save(existing);
    }

    @PatchMapping("/{id}/nickname")
    public ResponseEntity<String> updateNickname(@PathVariable long id, @RequestBody NicknameUpdateRequest request) {
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
            return ResponseEntity.ok("Spitzname gegeben. Aber gehen dir die Ideen aus .....");
        }

        return ResponseEntity.ok("Spitzname gegeben");
    }

    @PatchMapping("/{id}/box")
    public ResponseEntity<String> updateBox(@PathVariable long id, @RequestBody BoxUpdateRequest request) {
        OwnedPokemon pokemon = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        String newBoxName = request.getBoxName();
        String currentBoxName = pokemon.getBox() != null ? pokemon.getBox().getName() : null;

        if (!Objects.equals(newBoxName, currentBoxName)) {
            Box newBox = boxRepo.findByName(newBoxName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box nicht gefunden"));

            long count = ownedRepo.countByBox_Name(newBoxName);
            if (count >= 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die Box ist bereits voll");
            }

            pokemon.setBox(newBox);
            ownedRepo.save(pokemon);
        }

        return ResponseEntity.ok("Box geändert auf: " + newBoxName);
    }

    @PatchMapping("/{id}/edition")
    public ResponseEntity<String> updateEdition(@PathVariable long id, @RequestBody EditionUpdateRequest request) {
        OwnedPokemon pokemon = ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        Edition edition = request.getEdition();
        if (edition == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Edition darf nicht leer sein");
        }

        pokemon.setEdition(edition);
        ownedRepo.save(pokemon);

        return ResponseEntity.ok("Edition geändert auf: " + request.getEdition());
    }

    @GetMapping("/edition/{edition}")
    public List<OwnedPokemon> getByEdition(@PathVariable Edition edition) {
        return ownedRepo.findByEdition(edition);
    }

    private void checkBoxCapacity(String boxName) {
        long count = ownedRepo.countByBox_Name(boxName);
        if (count >= 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die Ausgewählte Box ist voll");
        }
    }
}
