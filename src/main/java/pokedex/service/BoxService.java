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

/**
 * Service zur Verwaltung von Boxen und zum Verschieben von Pokemon zwischen den Boxen
 */
@Service
public class BoxService {

    private final BoxRepository boxRepo;
    private final OwnedPokemonRepository ownedRepo;

    public BoxService(BoxRepository boxRepo, OwnedPokemonRepository ownedRepo) {
        this.boxRepo = boxRepo;
        this.ownedRepo = ownedRepo;
    }

    /**
     * Gibt alle Boxen zurück
     * @return Liste aller Boxen
     */
    public List<Box> getAllBoxes() {
        return boxRepo.findAll();
    }

    /**
     * Gibt eine Box anhand dessen Namens zurück.
     * @param name der Name der Box (TEAM /B OX1 ...)
     * @return Die gefundene Box
     * @throws ResponseStatusException Wenn di eBox nicht gefunden wird
     */
    public Box getBoxByName(BoxName name) {
        return boxRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));
    }

    /**
     * Prüft, ob die box voll ist.
     * @param name Der Name der Box
     * @return true, wenn die Box voll ist
     */
    public boolean isFull(BoxName name) {
        Long count = boxRepo.countByName(name);

        if (name == BoxName.TEAM) {
            return count >= 6;
        } else {
            return count >= 20;
        }
    }

    /**
     * Prüft, ob eine Box voll istund wirft einen Fehler, falls dies der Fall wäre
     * @param name Der Name der Box
     * @throws ResponseStatusException  Wenn die Box voll ist
     */
    public void checkCapacity(BoxName name) {
        if (isFull(name)) {
            String message = (name == BoxName.TEAM)
                    ? "Team ist schon voll (max. 6 Pokemon)"
                    : "Box ist schon voll (max. 20 Pokemon)";
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
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
        if (sourceBox.equals(targetBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Boxen sind identisch");
        }

        OwnedPokemon pokemon = ownedRepo.findById(pokemonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokemon nicht gefunden"));


        Box target = getBoxByName(targetBox);

        if (!pokemon.getBox().getName().equals(sourceBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pokemon befindet sich nicht in dieser Box");
        }

        if (isFull(targetBox)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ziel Box ist schon voll");
        }

        //Speichert die verschiebung
        pokemon.setBox(target);
        ownedRepo.save(pokemon);
    }
}
