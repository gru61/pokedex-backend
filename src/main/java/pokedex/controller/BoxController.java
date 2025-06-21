package pokedex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokedex.model.box.Box;
import pokedex.model.box.BoxName;
import pokedex.service.BoxService;

@RestController
@RequestMapping("/api/box")
public class BoxController {


    private final BoxService boxService;

    @Autowired
    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping("/{name}")
    public Box getBoxByName(@PathVariable BoxName name) {
        return boxService.getBoxByName(name);
    }

    @GetMapping("/{name}/isfull")
    public boolean isFull(@PathVariable BoxName name) {
        return boxService.isFull(name);
    }
}
