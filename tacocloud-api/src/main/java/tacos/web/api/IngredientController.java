package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(
        path = "/ingredients",
        produces = "application/json"
)
@CrossOrigin(origins = "*")
public class IngredientController {

    private IngredientRepository ingredientRepo;

    @Autowired
    public IngredientController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @GetMapping
    public Iterable<Ingredient> allIngredients() {
        return ingredientRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Ingredient> byId(@PathVariable String id) {
        return ingredientRepo.findById(id);
    }

    @PutMapping("/{id}")
    public void updateIngredient(
            @PathVariable String id,
            @RequestBody Ingredient ingredient
    ) {
        if (!ingredient.getId().equals(id)) {
            throw new IllegalStateException(
                    "Given ingredient's ID doesn't match the ID in the path."
            );
        }

        ingredientRepo.save(ingredient);
    }

    @PostMapping
    public ResponseEntity<Ingredient> postIngredient(@RequestBody Ingredient ingredient) {
        Ingredient saved = ingredientRepo.save(ingredient);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                URI.create("http://localhost:8080/ingredients/" +
                                   ingredient.getId())
        );

        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable String id) {
        ingredientRepo.deleteById(id);
    }

}
