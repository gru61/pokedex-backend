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

import java.util.Arrays;
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

        BoxName requestedBox = request.getBox();

        if (requestedBox == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box darf nicht leer sein");
        }

        BoxName finalBox = requestedBox;

        if (!requestedBox.equals(BoxName.TEAM)) {
            if (ownedRepo.countByBox_Name(requestedBox) >= 20) {
                finalBox = findNextAvailableBox();
            }
        } else {
            if (ownedRepo.countByBox_Name(requestedBox) >= 6) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Du kannst nicht mehr als 6 Pokemon tragen");
            }
        }

        Box box = boxRepo.findByName(finalBox)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));

        OwnedPokemon pokemon = new OwnedPokemon(
                species,
                request.getNickname(),
                request.getLevel(),
                request.getEdition(),
                box
        );
        return ownedRepo.save(pokemon);
    }

    private void checkCapacity(BoxName boxName) {
        long count = ownedRepo.countByBox_Name(boxName);

        if (boxName.equals(BoxName.TEAM) && count >= 6) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Du kannst nicht mehr als 6 Pokemon bei dir tragen");
        } else if (count >= 20) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Die Box ist voll, dein Pokemon wird in die nächst freien BOX weiter geleitet");
        }
    }

    private BoxName findNextAvailableBox() {
        return Arrays.stream(BoxName.values())
                .filter(box -> !box.equals(BoxName.TEAM))
                .filter(box ->ownedRepo.countByBox_Name(box) <20)
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT, "Du kannst keine weitere Pokemon hinzufügen"));
    }


    public OwnedPokemon updatePokemon(long id, OwnedPokemonRequest request) {
        OwnedPokemon existing = getPokemonById(id);

        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level kann nicht gesenkt werden");
        }

        PokemonSpecies species = speciesRepo.findById(request.getSpeciesId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ungültiger speciesId"));

        BoxName newBoxName = request.getBox();
        BoxName currentBoxName = existing.getBox() != null ? existing.getBox().getName() : null;

        if (!Objects.equals(newBoxName, currentBoxName)) {
            checkCapacity(newBoxName);
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
        OwnedPokemon existing = getPokemonById(id);

        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dein Pokemon kann nicht schwächer werden");
        }

        existing.setLevel(request.getLevel());
        return ownedRepo.save(existing);
    }

    public String updateNickname(long id, NicknameUpdateRequest request) {
        OwnedPokemon pokemon = getPokemonById(id);

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
        OwnedPokemon pokemon = getPokemonById(id);

        BoxName newBoxName = request.getBoxName();
        BoxName currentBoxName = pokemon.getBox() != null ? pokemon.getBox().getName() : null;

        if (!Objects.equals(newBoxName, currentBoxName)) {
            Box newBox = boxRepo.findByName(newBoxName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box nicht gefunden"));

            checkCapacity(newBoxName);
            pokemon.setBox(newBox);
            ownedRepo.save(pokemon);
        }

        return "Box geändert auf: " + newBoxName;
    }

    public String updateEdition(long id, EditionUpdateRequest request) {
        OwnedPokemon pokemon = getPokemonById(id);

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
        getPokemonById(id);
        ownedRepo.deleteById(id);
    }
}
