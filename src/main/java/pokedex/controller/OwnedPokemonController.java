package pokedex.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.dto.box.BoxUpdateRequest;
import pokedex.dto.ownedpokemon.EditionUpdateRequest;
import pokedex.dto.ownedpokemon.LevelUpdateRequest;
import pokedex.dto.ownedpokemon.NicknameUpdateRequest;
import pokedex.dto.ownedpokemon.OwnedPokemonRequest;
import pokedex.model.edition.Edition;
import pokedex.model.ownedpokemon.OwnedPokemon;
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

    @Operation(summary = "Gibt alle Gefangenen Pokemon als Liste zurück")
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen")
    @GetMapping("")
    public List<OwnedPokemon> getAllPokemon() {
        return ownedService.getAllPokemon();
    }

    @Operation(summary = "Holt das gesuchte Pokemon anhand dessen ID")
    @ApiResponse(responseCode = "200", description = "Pokemon gefunden")
    @ApiResponse(responseCode = "404", description = "Pokemon nicht gefunden")
    @GetMapping("/{id}")
    public OwnedPokemon getPokemonById(@PathVariable long id) {
        return ownedService.getPokemonById(id);
    }

    @Operation(summary = "Fügt ein neues gefangenes Pokemon hinzu")
    @ApiResponse(responseCode = "201", description = "Pokemon erfolgreich hinzugefügt")
    @ApiResponse(responseCode = "400", description = "Ungültige Eingabe Daten")
    @ApiResponse(responseCode = "409", description = "Ziel Box voll")
    @PostMapping("")
    public OwnedPokemon addPokemon(@RequestBody OwnedPokemonRequest request) {
        return ownedService.addPokemon(request);
    }

    @Operation(summary = "Update eines vorhanden gefangenen Pokemon")
    @ApiResponse(responseCode = "200", description = "Pokemon wurde aktualisiert")
    @ApiResponse(responseCode = "400", description = "Update fehlgeschlagen")
    @PutMapping("/{id}")
    public OwnedPokemon updatePokemon(@PathVariable long id, @Valid @RequestBody OwnedPokemonRequest request) {
        return ownedService.updatePokemon(id,request);
    }

    @Operation(summary = "Level Update des Ziel Pokemon's", description = "Nur erhöhung möglich")
    @ApiResponse(responseCode = "200", description = "Erfolgreich geändert")
    @ApiResponse(responseCode = "400", description = "Ungültiger Wert")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @PatchMapping("/{id}/level")
    public OwnedPokemon updateLevel(@PathVariable long id, @Valid @RequestBody LevelUpdateRequest request) {
        return ownedService.updateLevel(id,request);
    }

    @Operation(summary = "Fügt dem Pokemon einen Spitznamen zu")
    @ApiResponse(responseCode = "200", description = "Spitznamen erfolgreich gesetzt")
    @ApiResponse(responseCode = "400", description = "Ungültige Eingabe")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @PatchMapping("/{id}/nickname")
    public ResponseEntity<String> updateNickname(@PathVariable long id, @Valid @RequestBody NicknameUpdateRequest request) {
        return ResponseEntity.ok(ownedService.updateNickname(id, request));
    }

    @Operation(summary = "Verschiebt ein Pokemon in eine neue Box", description = "Überprüft automatisch die Ziel Box Kapazität")
    @ApiResponse(responseCode = "200", description = "Pokemon erfolgreich in die Box gesetzt")
    @ApiResponse(responseCode = "400", description = "Ungültige Ziel Box")
    @ApiResponse(responseCode = "404", description = "Pokemon oder Ziel Box nicht gefunden")
    @ApiResponse(responseCode = "409", description = "Ziel Box ist voll")
    @PatchMapping("/{id}/box")
    public ResponseEntity<String> updateBox(@PathVariable long id, @Valid @RequestBody BoxUpdateRequest request) {
        return ResponseEntity.ok(ownedService.updateBox(id,request));
    }

    @Operation(summary = "Ändert die Spielversion", description = "Ermöglicht das Tauschen und tracken der Pokemon")
    @ApiResponse(responseCode = "200", description = "Edition erfolgreich gesetzt")
    @ApiResponse(responseCode = "400", description = "Ungültige Edition")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @PatchMapping("/{id}/edition")
    public ResponseEntity<String> updateEdition(@PathVariable long id, @Valid @RequestBody EditionUpdateRequest request) {
        return ResponseEntity.ok(ownedService.updateEdition(id,request));
    }

    @Operation(summary = "Gibt alle Pokemon der gewünschten Edition zurück", description = "für die Suche nach Edition")
    @ApiResponse(responseCode = "200", description = "Pokemon liste nach Edition erfolgreich erstellt")
    @ApiResponse(responseCode = "404", description = "Kein Pokemon in dieser Edition gefunden")
    @GetMapping("/edition/{edition}")
    public List<OwnedPokemon> getByEdition(@PathVariable Edition edition) {
        return ownedService.getByEdition(edition);
    }

    @Operation(summary = "Entfernt das Pokemon aus dem Speicher", description = "löscht das Pokemon dauerhaft")
    @ApiResponse(responseCode = "200", description = "Pokemon erfolgreich gelöscht")
    @ApiResponse( responseCode = "404", description = "Pokemon nicht gefunden")
    @DeleteMapping("/{id}")
    public void deletePokemonById(@PathVariable long id) {
        ownedService.deletePokemonById(id);
    }

}
