package tacos.web.api;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import tacos.Ingredient;
import tacos.Ingredient.Type;

public class IngredientResource extends RepresentationModel<IngredientResource> {

    @Getter
    private final String name;

    @Getter
    private final Type type;

    public IngredientResource(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }

}
