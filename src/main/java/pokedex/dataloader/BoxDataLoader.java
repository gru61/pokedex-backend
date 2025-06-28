package pokedex.dataloader;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pokedex.model.box.BoxName;


/**
 * Lädt initiale Box Daten beim Anwendungsstart, falls sie noch nicht existieren.
 * Nutzt JDBCTemplate für direkte SQL-Operationen auf der Datenbank.
 */
@Component
@Transactional
public class BoxDataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public BoxDataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        Long count = jdbcTemplate.queryForObject("select count(*) from box", Long.class);
        if (count !=null && count >0) {
            System.out.println("Boxen wurden schon generiert");
            return;
        }
        createInitialBoxesIfNotExists();
    }

    private void createInitialBoxesIfNotExists() {
        for (BoxName boxName : BoxName.values()) {
            Long count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM box WHERE name =?", Long.class, boxName.name()
            );

            if (count == 0) {
                jdbcTemplate.update("INSERT INTO box (name) VALUES (?)", boxName.name());
                System.out.println("Box " + boxName + " wurde automatisch erstellt");
            } else {
                System.out.println("Box " + boxName + " ist bereits schon vorhanden");
            }
        }
    }
}

