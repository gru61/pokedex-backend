package pokedex.service.pokemonspecies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pokedex.exception.NotFoundException;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.model.type.PokemonType;
import pokedex.repository.pokemonspecies.PokemonSpeciesRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonSpeciesServiceTest {

    @Mock
    private PokemonSpeciesRepository speciesRepository;

    @InjectMocks
    private PokemonSpeciesService pokemonSpeciesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSpecies_shouldReturnListOfSpecies() {
        // Arrange
        List<PokemonSpecies> mockList = Arrays.asList(
                new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, PokemonType.GIFT),
                new PokemonSpecies(133L, "Evoli", PokemonType.NORMAL, null)
        );

        when(speciesRepository.findAll()).thenReturn(mockList);

        // Act
        List<PokemonSpecies> result = pokemonSpeciesService.getAllSpecies();

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.size());
        assertEquals("Bisasam", result.get(0).getName());
        verify(speciesRepository, times(1)).findAll();
    }

    @Test
    void getByName_shouldReturnMatchingSpecies() {
        // Arrange
        String name = "Bisasam";
        List<PokemonSpecies> mockResult = List.of(
                new PokemonSpecies(1L, name, PokemonType.PFLANZE, PokemonType.GIFT)
        );

        when(speciesRepository.findByName(name)).thenReturn(mockResult);

        // Act
        List<PokemonSpecies> result = pokemonSpeciesService.getByName(name);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
        assertEquals(PokemonType.PFLANZE, result.get(0).getType1());
        verify(speciesRepository, times(1)).findByName(name);
    }

    @Test
    void getByName_whenNoResults_shouldThrowNotFoundException() {
        // Arrange
        String name = "Mewtwo";

        when(speciesRepository.findByName(name)).thenReturn(List.of());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> pokemonSpeciesService.getByName(name));
        verify(speciesRepository, times(1)).findByName(name);
    }
}