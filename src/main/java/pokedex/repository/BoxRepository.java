package pokedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;

import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByName(BoxName name);

}
