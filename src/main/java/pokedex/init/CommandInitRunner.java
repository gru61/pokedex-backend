package pokedex.init;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import pokedex.model.Pokemon;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pokedex.repository.PokemonRepository;

import java.util.List;
import java.util.Set;


@Component
public class CommandInitRunner implements CommandLineRunner {

    private final PokemonRepository pokemonRepository;
    private final Validator validator;


    public CommandInitRunner(PokemonRepository pokemonRepository, Validator validator) {
        this.pokemonRepository = pokemonRepository;
        this.validator = validator;
    }

    @Override
    public void run(String... args) throws Exception {

        pokemonRepository.deleteAll();

        List<Pokemon> starter = List.of(

                new Pokemon(1,"Bisasam","Pflanze","Gift",30, 3 ),
                new Pokemon(6,"Glurak","Feuer","Flug",30, 3 ),
                new Pokemon(24,"Arbok","Normal","Gift",80, 3 ),
                new Pokemon(148,"Dratini","Drache","",100, 3 ),
                new Pokemon(51,"Digdri","Boden","",30, 3 ),
                new Pokemon(7,"Schiggy","Wasser","",20, 3 ),

                new Pokemon(200,"Glurak","Feuer","",30,10),
                new Pokemon(4,"Glumanda","","",15,1),
                new Pokemon(6,"","Pflanze","",15,1),
                new Pokemon(7,"Bisa","Pflanze","",120,1),
                new Pokemon(8,"Bisa","Feuer","",120,-5),

                new Pokemon(8,"Bisa","Feuer","",100,1),
                new Pokemon(1,"Bisa","Feuer","",101,0),
                new Pokemon(0,"Bisa","Feuer","",100,0),
                new Pokemon(5,"Bisa","","kdajsfg",100,0)
        );

        for (Pokemon p : starter) {
            Set<ConstraintViolation<Pokemon>> violations = validator.validate(p);
            if (violations.isEmpty()) {
                pokemonRepository.save(p);
            } else {
                System.out.println("❌ Ungültiges Pokemon: " + p);
                violations.forEach(v -> System.out.println(v.getMessage()));
            }
        }
        pokemonRepository.findAll().forEach(System.out::println);
    }
}