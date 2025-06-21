package pokedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.model.edition.Edition;
import pokedex.model.ownedpokemon.OwnedPokemon;

import java.util.List;

public interface OwnedPokemonRepository extends JpaRepository<OwnedPokemon, Long> {
    List<OwnedPokemon> findByBox(Box box);
    List<OwnedPokemon> findByEdition(Edition edition);
    List<OwnedPokemon> findBySpecies_Name(String name);
    List<OwnedPokemon> findBySpecies_PokedexId(int pokedexId);
    long countByNicknameAndIdNot(String nickname, Long id);
    long countByBox_Name(BoxName boxname);
}
