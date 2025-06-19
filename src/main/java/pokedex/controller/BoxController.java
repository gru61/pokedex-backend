package pokedex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pokedex.model.Box;
import pokedex.repository.BoxRepository;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class BoxController {


    private final BoxRepository boxRepo;

    @Autowired
    public BoxController(BoxRepository boxRepo) {
        this.boxRepo = boxRepo;
    }

    @GetMapping("")
    public List<Box> getAllBoxes() {
        return boxRepo.findAll();
    }

    @GetMapping("/{name}")
    public Box getBoxByName(@PathVariable String name) {
        return boxRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box nicht gefunden: " + name));
    }
}
