package pokedex.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.repository.BoxRepository;

import java.util.List;

@Service
public class BoxService {

    private final BoxRepository boxRepo;

    public BoxService(BoxRepository boxRepo) {
        this.boxRepo = boxRepo;
    }

    public List<Box> getAllBoxes() {
        return boxRepo.findAll();
    }

    public Box getBoxByName(BoxName name) {
        return boxRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden"));
    }

    public void checkCapacity(BoxName name) {
        if (boxRepo.countByName(name) >= 20) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Box ist schon voll");
        }
    }

    public boolean isFull(BoxName name) {
        return boxRepo.countByName(name) >= 20;
    }
}
