package kuvaldis.play.hateoas;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

// could also be IdentifiableResourceAssemblerSupport
public class PersonResourceAssembler extends ResourceAssemblerSupport<Person, PersonResource> {

    public PersonResourceAssembler() {
        super(PersonController.class, PersonResource.class);
    }

    @Override
    public PersonResource toResource(final Person person) {
        return createResourceWithId(person.getId(), person);
    }
}
