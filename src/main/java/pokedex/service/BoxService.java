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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service zur Verwaltung von Boxen und zum Verschieben von Pokemon zwischen den Boxen
 */
@Service
public class BoxService {

    private final BoxRepository boxRepo;
    private final OwnedPokemonRepository ownedRepo;

    private static final Logger logger = LoggerFactory.getLogger(BoxService.class);

    public BoxService(BoxRepository boxRepo, OwnedPokemonRepository ownedRepo) {
        this.boxRepo = boxRepo;
        this.ownedRepo = ownedRepo;
    }


    /**
     * Gibt eine Box anhand dessen Namens zurück.
     * @param name der Name der Box (TEAM /B OX1 ...)
     * @return Die gefundene Box
     * @throws ResponseStatusException Wenn di eBox nicht gefunden wird
     */
    public Box getBoxByName(BoxName name) {
        logger.info("Box mit dem Namen {} abgerufen", name);
        return boxRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));
    }

    /**
     * Prüft, ob die box voll ist.
     * @param name Der Name der Box
     * @return true, wenn die Box voll ist
     */
    public boolean isFull(BoxName name) {
        Box box = getBoxByName(name);
        Long pokemonCount = ownedRepo.countByBox(box);

        if (name == BoxName.TEAM) {
            return pokemonCount >= 6;
        } else {
            return pokemonCount >= 20;
        }
    }

    /**
     * Verschiebt ein Pokemon von einer Quell-Box in die Ziel-Box
     * @param sourceBox Die aktuelle Box des Pokemon
     * @param targetBox Die Ziel-Box, in die das Pokemon soll
     * @param pokemonId Die ID des zu verschiebenden Pokemon
     * @throws ResponseStatusException Bei ungültiger Eingabe oder Fehlern
     */
    @Transactional
    public void movePokemon(BoxName sourceBox, BoxName targetBox, Long pokemonId) {
        logger.info("Versuch Pokemon {} von {} nach {} zu verschieben", pokemonId, sourceBox, targetBox);

        //Validierung: Quell und Ziel Box dürfen nicht gleich sein
        if (sourceBox.equals(targetBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Boxen sind identisch");
        }

        //Pokemon laden
        OwnedPokemon pokemon = ownedRepo.findById(pokemonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));

        //Validierung: Pokemon muss sich in der Quell-Box befinden
        if (!pokemon.getBox().getName().equals(sourceBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pokemon befindet sich nicht in dieser Box");
        }
        //Ziel-Box laden
        Box target = getBoxByName(targetBox);

        //Validierung: Prüft ob der Ziel-Box voll ist
        if (isFull(targetBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ziel Box ist schon voll");
        }

        //Speichert die verschiebung
        pokemon.setBox(target);
        ownedRepo.save(pokemon);
        logger.info("Pokemon {} erfolgreich von {} nach {} verschoben", pokemonId, sourceBox, targetBox);
    }
}
