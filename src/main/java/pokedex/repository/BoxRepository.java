package pokedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pokedex.model.Box;

import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByName(String name);
}
