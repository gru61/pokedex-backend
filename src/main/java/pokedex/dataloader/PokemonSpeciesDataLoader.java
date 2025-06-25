package pokedex.dataloader;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;


@Component
public class PokemonSpeciesDataLoader implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public PokemonSpeciesDataLoader(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Long count = jdbcTemplate.queryForObject("select count(*) from pokemon_species", Long.class);
        if (count != null && count >0) {
            System.out.println("Pokedex ist schon auf dem aktuellsten Stand");
            return;
        }
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new  ClassPathResource("data/pokemon_species.sql"));
            System.out.println("Pokedex erfolgreich geladen");
        } catch (Exception e) {
            System.err.println("Fehler beim laden des Pokedexs" + e.getMessage());
        }
    }
}