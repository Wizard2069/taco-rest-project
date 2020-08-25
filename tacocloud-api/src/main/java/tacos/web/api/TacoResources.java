package tacos.web.api;

import org.springframework.hateoas.CollectionModel;

public class TacoResources extends CollectionModel<TacoResource> {

    public TacoResources(CollectionModel<TacoResource> tacoResources) {
        TacoResources.of(tacoResources);
    }

}
