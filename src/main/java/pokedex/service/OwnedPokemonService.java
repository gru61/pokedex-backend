package pokedex.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.dto.CreateOwnedRequest;
import pokedex.dto.UpdateOwnedRequest;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.ownedpokemon.OwnedPokemon;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.repository.OwnedPokemonRepository;
import pokedex.repository.PokemonSpeciesRepository;

import java.util.List;

/**
 * Service zu Verwaltung von gefangenen Pokemon.
 * Enthält Logik zum Hinzufügen, Bearbeiten und Validieren
 */
@Service
public class OwnedPokemonService {

    private final OwnedPokemonRepository ownedRepo;
    private final PokemonSpeciesRepository speciesRepo;
    private final BoxService boxService;
    private final EvolutionService evolutionService;

    public OwnedPokemonService(OwnedPokemonRepository ownedRepo, PokemonSpeciesRepository speciesRepo, BoxService boxService, EvolutionService evolutionService) {
        this.ownedRepo = ownedRepo;
        this.speciesRepo = speciesRepo;
        this.boxService = boxService;
        this.evolutionService = evolutionService;
    }


    /**
     * Gibt alle gefangenen Pokemon zurück.
     * @return Liste aller gefangenen Pokemon
     */
    public List<OwnedPokemon> getAllPokemon() {
        return ownedRepo.findAll();
    }


    /**
     * Gibt ein gefangenes Pokemon anhand desse ID zurück.
     * @param id Di eId des gesuchten Pokemon
     * @return Das gefundene Pokemon
     * @throws ResponseStatusException Wenn das Pokemon nich gefunden wird
     */
    public OwnedPokemon getPokemonById(long id) {
        return ownedRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));
    }

    /**
     * Fügt ein neues Pokemon hinzu
     * @param request Die Eingabedaten
     * @return Das gespeicherte Pokemon
     * @throws ResponseStatusException Wenn:
     * - Die Box nicht ausgewählt wurde
     * - Die Box schon voll ist
     */

    public OwnedPokemon addPokemon(CreateOwnedRequest request) {
        PokemonSpecies species = getValidSpecies(request.getSpeciesId());

        BoxName targetBoxName = request.getBox();
        if (targetBoxName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box muss angegeben werden");
        }

        //Prüfung, ob Zielbox voll ist.
        if (boxService.isFull(targetBoxName)) {
            String message = (targetBoxName == BoxName.TEAM)
                    ? "Team ist schon voll (max. 6 PPokemon"
                    : "Zielbox ist schon voll (max. 20 Pokemon";
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }

        var targetBox = boxService.getBoxByName(targetBoxName);

        OwnedPokemon pokemon = new OwnedPokemon(
                species,
                request.getNickname(),
                request.getLevel(),
                request.getEdition(),
                targetBox
        );

        return ownedRepo.save(pokemon);
    }


    /**
     * Aktualisiert ein gefangenes Pokemon
     * @param id Die ID des Pokemons
     * @param request Die neuen Daten
     * @return Das aktualisierte Pokemon
     */
    public OwnedPokemon updatePokemon(Long id, UpdateOwnedRequest request) {
        OwnedPokemon existing = getPokemonById(id);

        validateUpdate(existing, request);

        updateBoxIfChanged(existing, request.getBox());
        updateSpeciesIfChanged(existing, request.getSpeciesId());

        existing.setEdition(request.getEdition());
        existing.setLevel(request.getLevel());
        existing.setNickname(request.getNickname());

        return ownedRepo.save(existing);
    }


    /**
     * Prüft:
     * - Ob das Level gesenkt wird
     * - Ob die Zeil_Box dieselbe wie die aktuelle Box
     * @param existing Das aktuelle Pokemon
     * @param request Die neuen Eingabewerte
     */
    private void validateUpdate(OwnedPokemon existing, UpdateOwnedRequest request) {
        if (request.getLevel() < existing.getLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level kann nicht gesenkt werden");
        }

        if (request.getBox() != null && existing.getBox().getName().equals(request.getBox())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pokémon befindet sich bereits in dieser Box");
        }
    }

    /**
     * Lädt ein gültiges Pokemon per Pokedex-ID
     * @param speciesId Die Pokedex-ID des Pokemons
     * @return Das passende Pokemon, gemäss Pokedex-ID
     */
    private PokemonSpecies getValidSpecies(Long speciesId) {
        return speciesRepo.findById(speciesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Species nicht gefunden"));
    }

    /**
     * Prüft, ob sich das Pokémon entwickeln kann und führt diesen wenn möglich durch.
     * @param existing Das aktuelle Pokémon
     * @param newSpeciesId Die neue Pokedex-ID
     */
    private void updateSpeciesIfChanged(OwnedPokemon existing, Long newSpeciesId) {
        if (newSpeciesId == null || existing.getSpecies().getId().equals(newSpeciesId)) {
            return; // Keine Änderung notwendig
        }

        PokemonSpecies newSpecies = getValidSpecies(newSpeciesId);

        if (!evolutionService.isAllowedEvolution(existing.getSpecies().getPokedexId(), newSpecies.getPokedexId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Diese Entwicklung ist nicht erlaubt");
        }

        existing.setSpecies(newSpecies);
    }

    /**
     * Verschiebt ein Pokémon in eine andere Box, falls nötig.
     * @param existing     Das aktuelle Pokémon
     * @param requestedBox Die neue Box, falls angegeben
     */
    private void updateBoxIfChanged(OwnedPokemon existing, BoxName requestedBox) {
        if (requestedBox == null || existing.getBox().getName().equals(requestedBox)) {
            return;
        }

        if (boxService.isFull(requestedBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Zielbox ist voll");
        }

        Box newBox = boxService.getBoxByName(requestedBox);
        existing.setBox(newBox);
    }

    public void deletePokemonById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pokemon nicht gefunden");
        }
    }
}
