package pokedex;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Startet das Backend
 */
@SpringBootApplication
public class PokedexApp {

    public static void main(String[] args) {
        SpringApplication.run(PokedexApp.class,args);
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
