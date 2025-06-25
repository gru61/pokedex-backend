package pokedex.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.service.BoxService;

@RestController
@RequestMapping("/api/box")
public class BoxController {


    private final BoxService boxService;

    @Autowired
    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }


    @Operation(summary = "Lädt eine Box nach ihrem Namen", description = "Gibt als return alle Pokemon welche sich in der Box befindet")
    @ApiResponse(responseCode = "200", description = "Box wurde gefunden")
    @ApiResponse(responseCode = "404", description = "Box wurde nicht gefunden")
    @GetMapping("/{name}")
    public Box getBoxByName(@PathVariable BoxName name) {
        return boxService.getBoxByName(name);
    }


    @Operation(summary = "Überprüft ob die Ziel Box voll ist", description = "return true wenn voll")
    @ApiResponse(responseCode = "200", description = "Ergebnis wurde erfolgreich geladen")
    @ApiResponse(responseCode = "404", description = "Box wurde nicht gefunden")
    @GetMapping("/{name}/isfull")
    public boolean isFull(@PathVariable BoxName name) {
        return boxService.isFull(name);
    }


    @Operation(summary = "Verschiebt ein Pokemon von Box... zu Box...", description = "Zentraler Endpunkt für drag & drop")
    @ApiResponse(responseCode = "200", description = "Erfolgreich verschoben")
    @ApiResponse(responseCode = "404", description = "Box oder Pokemon nicht gefunden")
    @ApiResponse(responseCode = "409", description = "Ziel Box ist voll | Quellbox gleich Ziel Box")
    @PutMapping("/{sourceBox}/move-to/{targetBox}/{pokemonId}")
    public ResponseEntity<String> movePokemon (@PathVariable BoxName sourceBox, @PathVariable BoxName targetBox, @PathVariable Long pokemonId) {
        boxService.movePokemon(sourceBox,targetBox,pokemonId);
        return ResponseEntity.ok("Pokemon wurde verschoben");
    }

}
