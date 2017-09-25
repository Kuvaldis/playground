package kuvaldis.play.hateoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEntityLinks
@RestController
@RequestMapping("/hateoas")
public class HateoasTestController {

    @GetMapping("ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("builder-test")
    public Link builderTest() {
        return ControllerLinkBuilder.linkTo(PersonController.class)
                .withRel("people-but-not-quite");
    }

    @GetMapping("builder-test-2")
    public Link builderTest2() {
        return ControllerLinkBuilder.linkTo(PersonController.class)
                .slash("100500")
                .withSelfRel();
    }

    @GetMapping("builder-test-3")
    public Link builderTest3() {
        return ControllerLinkBuilder.linkTo(PersonController.class)
                .slash(new Person(321L))
                .withSelfRel();
    }

    @GetMapping("builder-method-test")
    public Link builderMethodTest() {
        return ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(PersonController.class).show(126L))
                .withSelfRel();
    }

    @Autowired
    private EntityLinks entityLinks;

    @GetMapping("entity-link-test")
    public Link entityLinkTest() {
        return entityLinks.linkToSingleResource(Order.class, 231L);
    }

    @GetMapping("resource-assembler-test")
    public Link resourceAssembler() {
        final Person person = new Person(5L);
        final PersonResourceAssembler assembler = new PersonResourceAssembler();
        final PersonResource resource = assembler.toResource(person);
        return resource.getLink(Link.REL_SELF);
    }
}
