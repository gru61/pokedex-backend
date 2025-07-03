package pokedex.service.evolution;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class EvolutionServiceIntegrationTest {

    private EvolutionService evolutionService;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        evolutionService = new EvolutionService(objectMapper);
    }

    @Test
    void testErlaubteEntwicklung_BisasamZuBisaknosp() {
        assertTrue(evolutionService.isAllowedEvolution(1, 2)); // Bisasam zu Bisaknosp
    }

    @Test
    void testErlaubteMehrfachentwicklung_Evoli() {
        assertTrue(evolutionService.isAllowedEvolution(133, 134)); // Evoli zu Aquana
        assertTrue(evolutionService.isAllowedEvolution(133, 135)); // Evoli zu Blitza
        assertTrue(evolutionService.isAllowedEvolution(133, 136)); // Evoli zu Flamara
    }

    @Test
    void testKeineUnerlaubteEntwicklung() {
        assertFalse(evolutionService.isAllowedEvolution(1, 6)); // Bisasam zu Glurak
        assertFalse(evolutionService.isAllowedEvolution(4, 2)); // Glumanda zu Bisaknosp
    }

    @Test
    void testMehrstufigeEntwicklung() {
        assertTrue(evolutionService.isAllowedEvolution(147, 148)); // Dratini zu Dragonir
        assertTrue(evolutionService.isAllowedEvolution(148, 149)); // Dragonir zu Dragoran
    }
}