package pokedex.service.ownedpokemon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pokedex.dto.ownedpokemon.CreateOwnedRequest;
import pokedex.dto.ownedpokemon.UpdateOwnedRequest;
import pokedex.exception.*;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;
import pokedex.model.ownedpokemon.OwnedPokemon;
import pokedex.model.pokemonspecies.PokemonSpecies;
import pokedex.model.type.PokemonType;
import pokedex.repository.ownedpokemon.OwnedPokemonRepository;
import pokedex.repository.pokemonspecies.PokemonSpeciesRepository;
import pokedex.service.box.BoxService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class OwnedPokemonServiceTest {

    @InjectMocks
    private OwnedPokemonService ownedPokemonService;

    @Mock
    private OwnedPokemonRepository ownedRepo;

    @Mock
    private PokemonSpeciesRepository speciesRepo;

    @Mock
    private BoxService boxService;

    @Captor
    private ArgumentCaptor<OwnedPokemon> pokemonCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- addPokemon() Tests ---

    @Test
    void shouldAddPokemonSuccessfully() {
        // Arrange
        CreateOwnedRequest request = new CreateOwnedRequest(1L, 7, Edition.ROT, BoxName.BOX11, "Grüene");

        PokemonSpecies species = new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, null);
        when(speciesRepo.findById(1L)).thenReturn(Optional.of(species));
        when(boxService.isFull(BoxName.BOX11)).thenReturn(false);
        when(boxService.getBoxByName(BoxName.BOX11)).thenReturn(new Box(BoxName.BOX11));

        // Act
        ownedPokemonService.addPokemon(request);

        // Assert
        verify(ownedRepo).save(pokemonCaptor.capture());
        OwnedPokemon saved = pokemonCaptor.getValue();

        assertThat(saved.getNickname()).isEqualTo("Grüene");
        assertThat(saved.getLevel()).isEqualTo(7);
        assertThat(saved.getEdition()).isEqualTo(Edition.ROT);
        assertThat(saved.getBox().getName()).isEqualTo(BoxName.BOX11);
    }

    @Test
    void addPokemon_shouldThrowNotFoundException_whenSpeciesNotFound() {
        // Arrange
        CreateOwnedRequest request = new CreateOwnedRequest(999L, 10, Edition.ROT, BoxName.BOX11, "NIXDA");
        when(speciesRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> ownedPokemonService.addPokemon(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Kein Pokemon mit der Pokedex-ID");
    }

    @Test
    void addPokemon_shouldThrowBoxFullException_whenBoxIsFull() {
        // Arrange
        CreateOwnedRequest request = new CreateOwnedRequest(1L, 10, Edition.ROT, BoxName.BOX11, "BLUE");
        PokemonSpecies species = new PokemonSpecies(1L, "Bisasam",  PokemonType.PFLANZE, null);
        when(speciesRepo.findById(1L)).thenReturn(Optional.of(species));
        when(boxService.isFull(BoxName.BOX11)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> ownedPokemonService.addPokemon(request))
                .isInstanceOf(BoxFullException.class)
                .hasMessageContaining("Zielbox ist schon voll (max. 20 Pokemon)");
    }

    // --- updatePokemon() Tests ---

    @Test
    void updatePokemon_shouldChangeBoxSuccessfully() {
        // Arrange
        Box oldBox = new Box(BoxName.BOX1);
        Box newBox = new Box(BoxName.BOX11);
        PokemonSpecies species = new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, null);
        OwnedPokemon existing = new OwnedPokemon(species, "AltesPoke", 35, Edition.ROT, oldBox);

        when(ownedRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(boxService.isFull(BoxName.BOX11)).thenReturn(false);
        when(boxService.getBoxByName(BoxName.BOX11)).thenReturn(newBox);

        UpdateOwnedRequest request = UpdateOwnedRequest.builder()
                .level(40)
                .edition(Edition.ROT)
                .box(BoxName.BOX11)
                .build();

        // Act
        ownedPokemonService.updatePokemon(1L, request);

        // Assert
        verify(ownedRepo).save(existing);
        assertThat(existing.getBox().getName()).isEqualTo(BoxName.BOX11);
        assertThat(existing.getEdition()).isEqualTo(Edition.ROT);
        assertThat(existing.getLevel()).isEqualTo(40);
    }

    @Test
    void updatePokemon_shouldThrowInvalidUpdateException_whenLevelIsLowered() {
        // Arrange
        Box box = new Box(BoxName.BOX11);
        PokemonSpecies species = new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, null);
        OwnedPokemon existing = new OwnedPokemon(species, "Blubber", 10, Edition.ROT, box);
        when(ownedRepo.findById(1L)).thenReturn(Optional.of(existing));

        UpdateOwnedRequest request = UpdateOwnedRequest.builder()
                .level(7)
                .edition(Edition.ROT)
                .box(BoxName.BOX11)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> ownedPokemonService.updatePokemon(1L, request))
                .isInstanceOf(InvalidUpdateException.class)
                .hasMessageContaining("Level kann nicht gesenkt werden");
    }

    // --- getPokemonById() Tests ---

    @Test
    void getPokemonById_shouldReturnPokemon_whenFound() {
        // Arrange
        OwnedPokemon pokemon = new OwnedPokemon(
                new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, null),
                "Finder",
                42,
                Edition.ROT,
                new Box(BoxName.BOX11)
        );
        when(ownedRepo.findById(1L)).thenReturn(Optional.of(pokemon));

        // Act
        OwnedPokemon result = ownedPokemonService.getPokemonById(1L);

        // Assert
        assertThat(result).isEqualTo(pokemon);
    }

    @Test
    void getPokemonById_shouldThrowNotFoundException_whenNotExists() {
        // Arrange
        when(ownedRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> ownedPokemonService.getPokemonById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pokemon nicht gefunden");
    }

    // --- deletePokemonById() Tests ---

    @Test
    void deletePokemonById_shouldCallDelete_whenPokemonExists() {
        // Arrange
        OwnedPokemon pokemon = new OwnedPokemon(
                new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, null),
                "Sternenstaub",
                100,
                Edition.ROT,
                new Box(BoxName.BOX11)
        );
        when(ownedRepo.findById(1L)).thenReturn(Optional.of(pokemon));

        // Act
        ownedPokemonService.deletePokemonById(1L);

        // Assert
        verify(ownedRepo).delete(pokemon);
    }

    @Test
    void deletePokemonById_shouldThrowNotFoundException_whenPokemonDoesNotExist() {
        // Arrange
        when(ownedRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> ownedPokemonService.deletePokemonById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pokemon nicht gefunden");
    }

    // --- getAllPokemon() Tests ---

    @Test
    void getAllPokemon_shouldReturnListOfPokemons() {
        // Arrange
        List<OwnedPokemon> pokemons = List.of(
                new OwnedPokemon(new PokemonSpecies(1L, "Bisasam", PokemonType.PFLANZE, null), "weedy", 5, Edition.ROT, new Box(BoxName.BOX11)),
                new OwnedPokemon(new PokemonSpecies(2L, "Shiggy", PokemonType.WASSER, null), "schiddy", 10, Edition.ROT, new Box(BoxName.BOX11))
        );
        when(ownedRepo.findAll()).thenReturn(pokemons);

        // Act
        List<OwnedPokemon> result = ownedPokemonService.getAllPokemon();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(pokemons);
    }
}