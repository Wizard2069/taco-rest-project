package tacos.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tacos.Ingredient;
import tacos.Taco;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TacoCloudClient {

    private RestTemplate restTemplate;

    private Traverson traverson;

    @Autowired
    public TacoCloudClient(
            RestTemplate restTemplate,
            Traverson traverson
    ) {
        this.restTemplate = restTemplate;
        this.traverson = traverson;
    }

    public List<Ingredient> getAllIngredients() {
        return restTemplate
                       .exchange(
                               "http://localhost:8080/ingredients",
                               HttpMethod.GET,
                               null,
                               new ParameterizedTypeReference<List<Ingredient>>() {}
                       )
                       .getBody();
    }

    // getForObject
    /*public Ingredient getIngredientById(String ingredientId) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);

        URI url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/ingredients/{id}")
                .build(urlVariables);

        *//*return restTemplate.getForObject(
                "http://localhost:8080/ingredients/{id}",
                Ingredient.class,
                // ingredientId
                urlVariables
        );*//*

        return restTemplate.getForObject(url, Ingredient.class);
    }*/

    // getForEntity
    public Ingredient getIngredientById(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity =
                restTemplate.getForEntity(
                        "http://localhost:8080/ingredients/{id}",
                        Ingredient.class,
                        ingredientId
                );

        log.info("Fetched time: " + responseEntity.getHeaders().getDate());

        return responseEntity.getBody();
    }

    // postForObject
    /*public Ingredient creatIngredient(Ingredient ingredient) {
        return restTemplate.postForObject(
                "http://localhost:8080/ingredients",
                ingredient,
                Ingredient.class
        );
    }*/

    // postForLocation
    /*public URI createIngredient(Ingredient ingredient) {
        return restTemplate.postForLocation(
                "http://localhost:8080/ingredients",
                ingredient
        );
    }*/

    // postForEntity
    public Ingredient createIngredient(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity =
                restTemplate.postForEntity(
                        "http://localhost:8080/ingredients",
                        ingredient,
                        Ingredient.class
                );

        log.info("New resource created at: " + responseEntity.getHeaders().getLocation());

        return responseEntity.getBody();
    }

    public void updateIngredient(Ingredient ingredient) {
        restTemplate.put(
                "http://localhost:8080/ingredients/{id}",
                ingredient,
                ingredient.getId()
        );
    }

    public void deleteIngredient(Ingredient ingredient) {
        restTemplate.delete(
                "http://localhost:8080/ingredients/{id}",
                ingredient.getId()
        );
        log.info("Deleted ingredient: " + ingredient);
    }

    // Traverson with restTemplate examples

    public Iterable<Ingredient> getAllIngredientsWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType =
                new ParameterizedTypeReference<>() {};

        CollectionModel<Ingredient> ingredientRes =
                traverson.follow("ingredients")
                         .toObject(ingredientType);

        Collection<Ingredient> ingredients = ingredientRes.getContent();

        return ingredients;
    }

    public Iterable<Taco> getRecentTacosWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Taco>> tacoType =
                new ParameterizedTypeReference<>() {};

        CollectionModel<Taco> tacoRes =
                traverson
                        .follow("tacos", "recents")
                         // .follow("recents")
                        .toObject(tacoType);

        Collection<Taco> tacos = tacoRes.getContent();

        return tacos;
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        String ingredientsUrl = traverson
                .follow("ingredients")
                .asLink()
                .getHref();

        return restTemplate.postForObject(
                ingredientsUrl,
                ingredient,
                Ingredient.class
        );
    }

}
