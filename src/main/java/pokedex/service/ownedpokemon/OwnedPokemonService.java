package pokedex.service.ownedpokemon;


import pokedex.exception.BoxFullException;
import pokedex.exception.InvalidEvolutionException;
import pokedex.exception.InvalidUpdateException;
import pokedex.exception.NotFoundException;
import org.springframework.stereotype.Service;
import pokedex.dto.ownedpokemon.CreateOwnedRequest;
import pokedex.dto.ownedpokemon.UpdateOwnedRequest;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.ownedpokemon.OwnedPokemon;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.repository.ownedpokemon.OwnedPokemonRepository;
import pokedex.repository.pokemonspecies.PokemonSpeciesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pokedex.service.evolution.EvolutionService;
import pokedex.service.box.BoxService;

import java.util.List;
import java.util.Optional;

/**
 * Service zu Verwaltung von gefangenen Pokemon.
 * Enthält Logik zum Hinzufügen, Bearbeiten und Validieren
 */
@Service
public class OwnedPokemonService {


    private static final Logger logger = LoggerFactory.getLogger(OwnedPokemonService.class);


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
     * Gibt ein gefangenes Pokemon anhand dessen ID zurück.
     * @param id Die Id des gesuchten Pokemon
     * @return Das gefundene Pokemon
     * @throws NotFoundException Wenn das Pokemon nicht gefunden wird
     */
    public OwnedPokemon getPokemonById(long id) {
        logger.info("Suche das gefangene Pokemon per dessen ID. {}", id);
        return ownedRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Pokemon nicht gefunden"));
    }

    /**
     * Fügt ein neues Pokemon hinzu
     * @param request Die Eingabedaten
     * @return Das gespeicherte Pokemon
     * @throws NotFoundException Wenn kein Pokemon mit der Pokedex-ID gefunden wurde
     * @throws IllegalStateException Wenn die Ziel-Box nicht angegeben wurde
     * @throws BoxFullException Wenn die Ziel-Box schon voll ist
     */
    public OwnedPokemon addPokemon(CreateOwnedRequest request) {
        logger.info("Füge ein neues gefangenes Pokemon hinzu: {}", request);

        // Lade die Species Daten des gefangenen Pokemon
        PokemonSpecies species = speciesRepo.findById(request.getPokedexId())
                .orElseThrow(() ->new NotFoundException("Kein Pokemon mit der Pokedex-ID " + request.getPokedexId() + " gefunden"));

        // Validierung der Zielbox
        BoxName targetBoxName = request.getBox();
        if (targetBoxName == null) {
            logger.warn("Box wurde nicht angegeben");
            throw new IllegalStateException("Box muss angegeben werden");
        }

        //Prüfung, ob Zielbox voll ist.
        if (boxService.isFull(targetBoxName)) {
            String message = (targetBoxName == BoxName.TEAM)
                    ? "Team ist schon voll (max. 6 Pokemon)"
                    : "Zielbox ist schon voll (max. 20 Pokemon)";
            logger.warn(message);
            throw new BoxFullException(message);
        }

        // Erstellt einen neuen Pokemon Eintrag
        Box targetBox = boxService.getBoxByName(targetBoxName);
        OwnedPokemon pokemon = new OwnedPokemon(
                species,
                request.getNickname(),
                request.getLevel(),
                request.getEdition(),
                targetBox
        );

        logger.info("Neues Pokemon erfolgreich hinzugefügt: {}", pokemon);
        return ownedRepo.save(pokemon);
    }


    /**
     * Aktualisiert ein gefangenes Pokemon
     * @param id Die ID des Pokemons
     * @param request Die neuen Daten
     * @return Das aktualisierte Pokemon
     */
    public OwnedPokemon updatePokemon(Long id, UpdateOwnedRequest request) {
        logger.info("Aktualisiere Pokemon mit der ID {} : {}", id, request);

        // Lade das bestehende Pokemon
        OwnedPokemon existing = getPokemonById(id);

        // Validiere die Eingabe
        validateUpdate(existing, request);

        // Aktualisiere Box und Spezies, falls nötig
        updateBoxIfChanged(existing, request.getBox());
        updateSpeciesIfChanged(existing, request.getPokedexId());


        // Aktualisiere die restlichen Werte
        existing.setEdition(request.getEdition());
        existing.setLevel(request.getLevel());
        existing.setNickname(request.getNickname());

        logger.info("Pokemon erfolgreich aktualisiert: {}", existing);
        return ownedRepo.save(existing);
    }


    /**
     * Prüft:
     * - Ob das Level gesenkt wird
     * - Ob die Zeil_Box dieselbe wie die aktuelle Box
     * @param existing Das aktuelle Pokemon
     * @param request Die neuen Eingabewerte
     * @throws InvalidUpdateException:
     * - Wenn versucht wird das Level zu senken
     * - Wenn das Pokemon sich in einer Box befindet und ob es in der Ziel-Box nicht schon vorhanden ist
     */
    private void validateUpdate(OwnedPokemon existing, UpdateOwnedRequest request) {
        // Validierung, ob das Level nicht gesenkt wird
        if (request.getLevel() < existing.getLevel()) {
            logger.warn("Versuch, das Level eines gefangenen Pokemon zu senken: {}", existing);
            throw new InvalidUpdateException("Level kann nicht gesenkt werden");
        }

        // Validierung, ob sich das Pokemon in einer Box sich befindet und es sich nicht in der gleichen Box existiert
        if (request.getBox() != null && request.getBox().equals(existing.getBox().getName())) {
            logger.warn("Pokemon befindet sich bereits in dieser Box: {}",  existing);
            throw new InvalidUpdateException("Pokémon befindet sich bereits in dieser Box");
        }
    }


    /**
     * Lädt ein gültiges Pokemon per Pokedex-ID
     * @param pokedexId Die Pokedex-ID des Pokemons
     * @return Das passende Pokemon, gemäss Pokedex-ID
     * @throws NotFoundException Wenn das Pokemon per Pokedex-ID nicht gefunden wurde
     */
    private PokemonSpecies getValidSpecies(Long pokedexId) {
        logger.info("Lade das Pokemon per Pokedex-ID {}", pokedexId);

        Optional<PokemonSpecies> species = speciesRepo.findById(pokedexId);
        if (species.isEmpty()) {
            logger.warn("Pokemon per Pokedex-IDS nicht gefunden: ID={}", pokedexId);
            throw new NotFoundException("Pokemon nicht gefunden");
        }
        return species.get();
    }


    /**
     * Prüft, ob sich das Pokémon entwickeln kann und führt diesen wenn möglich durch.
     * @param existing Das aktuelle Pokémon
     * @param newPokedexId Die neue Pokedex-ID
     * @throws InvalidEvolutionException Wenn die Entwicklung nihct erlaubt oder möglich ist
     */
    private void updateSpeciesIfChanged(OwnedPokemon existing, Long newPokedexId) {
        // Validierung, ob eine Änderung stattgefunden hat
        if (newPokedexId == null || existing.getSpecies().getId().equals(newPokedexId)) {
            logger.debug("Keine Änderungen Pokemon gefunden: {}", existing);
            return;
        }

        logger.info("Prüfe Entwicklung für Pokemon: {} -> Neue Pokedex-ID: {}", existing, newPokedexId);

        // Ladet das neue Pokemon
        PokemonSpecies newSpecies = getValidSpecies(newPokedexId);

        if (!evolutionService.isAllowedEvolution(existing.getSpecies().getPokedexId(), newSpecies.getPokedexId())) {
            logger.warn("Ungültige Entwicklung dür Pokemon: {}", existing);
            throw new InvalidEvolutionException("Diese Entwicklung ist nicht erlaubt");
        }

        existing.setSpecies(newSpecies);
        logger.info("Entwicklung erfolgreich durchgeführt. {}",  existing);
    }


    /**
     * Verschiebt ein Pokémon in eine andere Box, falls nötig.
     * @param existing     Das aktuelle Pokémon
     * @param requestedBox Die neue Box, falls angegeben
     * @throws BoxFullException Wenn die Ziel-Box voll ist
     */
    private void updateBoxIfChanged(OwnedPokemon existing, BoxName requestedBox) {
        if (requestedBox == null || existing.getBox().getName().equals(requestedBox)) {
            logger.info("Keine Änderung der Box erforderlich für Pokemon: {}", existing);
            return;
        }

        logger.info("Verschiebe das Pokemon in eine neue Box: {} -> {}", existing, requestedBox);

        if (boxService.isFull(requestedBox)) {
            logger.warn("Ziel-Box ist voll: {}", requestedBox);
            throw new BoxFullException("Zielbox ist voll");
        }

        Box newBox = boxService.getBoxByName(requestedBox);
        existing.setBox(newBox);
        logger.info("Pokemon erfolgreich verschoben: {}", existing);
    }

    /**
     * Löscht ein Pokemon anhand dessen ID
     * @param id Die ID das zu löschenden Pokemon
     */
    public void deletePokemonById(Long id) {
        logger.info("Lösche Pokemon anhand der ID {}", id);

        OwnedPokemon pokemon = getPokemonById(id);
        ownedRepo.delete(pokemon);

        logger.info("Pokemon erfolgreich gelöscht: {}", pokemon);
    }
}
