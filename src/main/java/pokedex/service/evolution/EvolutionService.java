package pokedex.service.evolution;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pokedex.exception.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Service zur Prüfung der Entwicklungen der Pokemon
 */
@Service
public class EvolutionService {


    private static final Logger logger = LoggerFactory.getLogger(EvolutionService.class);

    private final Map<Integer, List<Integer>> evolutionRules;


    /**
     * Lädt die Datei für die Entwicklungsregeln und initialisiert diese
     * @param objectMapper Der ObjectMapper zum Parsen (umwandlung als JAVA-Objekt) der JSON-Datei
     * @throws InitializationException Gibt an, ob ein Fehler beim Initialisieren der Datei aufgetreten ist
     */
    public EvolutionService(ObjectMapper objectMapper) {
        try {
            //Versucht die JSON Datei aus dem PATH zu laden
            InputStream is = getClass().getClassLoader().getResourceAsStream("first_gen_evolutions_rules/evolutions.json");
            if (is == null) {
                throw new InitializationException("Die Datei wurde nicht gefunden");
            }

            // Parse die JSON Datei in eine typensichere Map
            this.evolutionRules = objectMapper.readValue(is, new TypeReference<>() {});
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Entwicklungsregeln: {}", e.getMessage(),e);
            throw new InitializationException("Fehler beim initialisieren des EvolutionsService",e);
        }
    }


    /**
     * Prüfung, ob eine Entwicklung von einer Pokedex-ID zur nächsten ID erlaubt ist
     *
     * @param currentSpeciesId Die aktuelle Pokedex-ID des Pokemons
     * @param targetSpeciesId Die neue Pokedex-ID nach der Entwicklung
     * @return true, wenn die entwicklung erlaubt ist
     */
    public boolean isAllowedEvolution(Integer currentSpeciesId, Integer targetSpeciesId) {
        if (currentSpeciesId == null || targetSpeciesId == null) {
            logger.warn("Ungültige Eingabe: currentSpeciesId={} oder targetSpeciesId=null", currentSpeciesId);
            return false;
        }

        List<Integer> allowedTargets =  evolutionRules.get(currentSpeciesId);
        if (allowedTargets == null) {
            logger.warn("Keine Entwicklung für currentSpeciesId={} gefunden",  currentSpeciesId);
            return false;
        }

        // Prüft ob die Ziel-ID in den erlaubten Zielen enthalten sind z.B. 147 (Dratini) -> 148 (Dragonir)
        for (int allowedTarget : allowedTargets) {
            if (allowedTarget == targetSpeciesId) {
                logger.debug("Erlaubte Entwicklung gefunden: {} -> {}",  currentSpeciesId, targetSpeciesId);
                return true;
            }
        }

        logger.warn("Keine erlaubte Entwicklung gefunden: {} -> {}",  currentSpeciesId, targetSpeciesId);
        return false;
    }
}

