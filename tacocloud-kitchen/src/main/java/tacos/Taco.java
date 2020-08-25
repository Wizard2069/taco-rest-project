package tacos;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Taco {

    private Long id;

    private Date createdAt;

    private String name;

    private List<Ingredient> ingredients = new ArrayList<>();

}
