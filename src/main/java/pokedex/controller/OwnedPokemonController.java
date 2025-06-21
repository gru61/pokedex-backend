package pokedex.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.dto.*;
import pokedex.model.Edition;
import pokedex.model.OwnedPokemon;
import pokedex.service.OwnedPokemonService;

import java.util.List;


@RestController
@RequestMapping("/api/pokemon")
public class OwnedPokemonController {


    private final OwnedPokemonService ownedService;

    @Autowired
    public OwnedPokemonController(OwnedPokemonService ownedService) {

        this.ownedService = ownedService;
    }

    @GetMapping("")
    public List<OwnedPokemon> getAllPokemon() {
        return ownedService.getAllPokemon();
    }

    @GetMapping("/{id}")
    public OwnedPokemon getPokemonById(@PathVariable long id) {
        return ownedService.getPokemonById(id);
    }

    @PostMapping("")
    public OwnedPokemon addPokemon(@RequestBody OwnedPokemonRequest request) {
        return ownedService.addPokemon(request);
    }

    @PutMapping("/{id}")
    public OwnedPokemon updatePokemon(@PathVariable long id, @Valid @RequestBody OwnedPokemonRequest request) {
        return ownedService.updatePokemon(id,request);
    }

    @PatchMapping("/{id}/level")
    public OwnedPokemon updateLevel(@PathVariable long id, @Valid @RequestBody LevelUpdateRequest request) {
        return ownedService.updateLevel(id,request);
    }

    @PatchMapping("/{id}/nickname")
    public ResponseEntity<String> updateNickname(@PathVariable long id, @Valid @RequestBody NicknameUpdateRequest request) {
        return ResponseEntity.ok(ownedService.updateNickname(id, request));
    }

    @PatchMapping("/{id}/box")
    public ResponseEntity<String> updateBox(@PathVariable long id, @Valid @RequestBody BoxUpdateRequest request) {
        return ResponseEntity.ok(ownedService.updateBox(id,request));
    }

    @PatchMapping("/{id}/edition")
    public ResponseEntity<String> updateEdition(@PathVariable long id, @Valid @RequestBody EditionUpdateRequest request) {
        return ResponseEntity.ok(ownedService.updateEdition(id,request));
    }

    @GetMapping("/edition/{edition}")
    public List<OwnedPokemon> getByEdition(@PathVariable Edition edition) {
        return ownedService.getByEdition(edition);
    }

    @DeleteMapping("/{id}")
    public void deletePokemonById(@PathVariable long id) {
        ownedService.deletePokemonById(id);
    }

}
