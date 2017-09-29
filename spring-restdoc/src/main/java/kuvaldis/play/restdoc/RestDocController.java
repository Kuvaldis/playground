package kuvaldis.play.restdoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@RestController
@RequestMapping("/restdoc")
public class RestDocController {

    @GetMapping("ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/simple-test")
    public String simple() {
        return "simple-test";
    }

    @GetMapping("/hypermedia-links")
    public ResponseEntity<ResourceSupport> hypermediaLinks() {
        final ResourceSupport result = new ResourceSupport();
        result.add(new Link("some-url", "alpha"), new Link("another-url", "bravo"));
        if (ThreadLocalRandom.current().nextBoolean()) {
            result.add(new Link("random-url", "random"));
        }
        return ResponseEntity.ok(result);
    }

    public static class Person {
        public Contact contact;
    }

    public static class Contact {
        public String name;
        public String email;
    }

    @GetMapping("/response-fields")
    public ResponseEntity<Person> responseFields() {
        final Person person = new Person();
        person.contact = new Contact();
        person.contact.name = "Vasilii Pupkin";
        person.contact.email = "vpup@pupup.up";
        return ResponseEntity.ok(person);
    }

    @GetMapping("/path-params/{first}/{second}")
    public void pathParams(final @PathVariable("first") String first,
                           final @PathVariable("second") String second) {
        return;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestDocController.class, args);
    }
}
