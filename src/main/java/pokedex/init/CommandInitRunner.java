//package pokedex.init;
//
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validator;
//import pokedex.model.OwnedPokemon;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import pokedex.repository.PokemonRepository;
//
//import java.util.List;
//import java.util.Set;
//
//
//@Component
//public class CommandInitRunner implements CommandLineRunner {
//
//    private final PokemonRepository pokemonRepository;
//    private final Validator validator;
//
//
//    public CommandInitRunner(PokemonRepository pokemonRepository, Validator validator) {
//        this.pokemonRepository = pokemonRepository;
//        this.validator = validator;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        pokemonRepository.deleteAll();
//
//        List<OwnedPokemon> starter = List.of(
//
////                new OwnedPokemon(1,"Bisasam","Pflanze","Gift",30, 3 ),
////                new OwnedPokemon(6,"Glurak","Feuer","Flug",30, 3 ),
////                new OwnedPokemon(24,"Arbok","Normal","Gift",80, 3 ),
////                new OwnedPokemon(148,"Dratini","Drache","",100, 3 ),
////                new OwnedPokemon(51,"Digdri","Boden","",30, 3 ),
////                new OwnedPokemon(7,"Schiggy","Wasser","",20, 3 ),
////
////                new OwnedPokemon(200,"Glurak","Feuer","",30,10),
////                new OwnedPokemon(4,"Glumanda","","",15,1),
////                new OwnedPokemon(6,"","Pflanze","",15,1),
////                new OwnedPokemon(7,"Bisa","Pflanze","",120,1),
////                new OwnedPokemon(8,"Bisa","Feuer","",120,-5),
////
////                new OwnedPokemon(8,"Bisa","Feuer","",100,1),
////                new OwnedPokemon(1,"Bisa","Feuer","",101,0),
////                new OwnedPokemon(0,"Bisa","Feuer","",100,0),
////                new OwnedPokemon(5,"Bisa","","kdajsfg",100,0)
//        );
//
//        for (OwnedPokemon p : starter) {
//            Set<ConstraintViolation<OwnedPokemon>> violations = validator.validate(p);
//            if (violations.isEmpty()) {
//                pokemonRepository.save(p);
//            } else {
//                System.out.println("❌ Ungültiges Pokemon: " + p);
//                violations.forEach(v -> System.out.println(v.getMessage()));
//            }
//        }
//        pokemonRepository.findAll().forEach(System.out::println);
//    }
//}