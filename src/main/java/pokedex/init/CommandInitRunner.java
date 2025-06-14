package pokedex.init;


import pokedex.model.Pokemon;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pokedex.repository.PokemonRepository;




@Component
public class CommandInitRunner implements CommandLineRunner {

    private final PokemonRepository pokemonRepository;

    public CommandInitRunner(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        pokemonRepository.save(new Pokemon(1,"Bisasam","Pflanze","Gift",30, 3 ));
        pokemonRepository.save(new Pokemon(6,"Glurak","Feuer","Flug",30, 3 ));
        pokemonRepository.save(new Pokemon(24,"Arbok","Normal","Gift",80, 3 ));
        pokemonRepository.save(new Pokemon(148,"Dratini","Drache","",100, 3 ));
        pokemonRepository.save(new Pokemon(51,"Digdri","Boden","",30, 3 ));
        pokemonRepository.save(new Pokemon(7,"Schiggy","Wasser","",20, 3 ));


        pokemonRepository.findAll().forEach(System.out::println);



    }
}