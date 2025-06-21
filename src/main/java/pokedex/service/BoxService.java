package pokedex.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.Box;
import pokedex.repository.BoxRepository;

import java.util.List;


@Service
public class BoxService {

    private final BoxRepository boxRepo;

    public BoxService(BoxRepository boxRepo) {
        this.boxRepo = boxRepo;
    }

    public List<Box> getAllBoxes(){
        return boxRepo.findAll();
    }

    public Box getBoxByName(String name){
        return boxRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));
    }

    public void checkCapacity(String boxName) {
        long count = boxRepo.countByName(boxName);
        if(count >= 20) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Box ist schon voll");
        }
    }

    public boolean isFull(String boxName) {
        return boxRepo.countByName(boxName) >= 20;
    }
}
