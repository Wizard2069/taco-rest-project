package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import tacos.Taco;

@Component
public class TacoResourcesProcessor implements RepresentationModelProcessor<PagedModel<EntityModel<Taco>>> {

    private final EntityLinks links;

    @Autowired
    public TacoResourcesProcessor(EntityLinks links) {
        this.links = links;
    }

    @Override
    public PagedModel<EntityModel<Taco>> process(PagedModel<EntityModel<Taco>> resource) {
        resource.add(
                links.linkFor(Taco.class)
                     .slash("recent")
                     .withRel("recents")
        );

        return resource;
    }

}
