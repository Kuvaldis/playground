package kuvaldis.play.hateoas;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PersonController {

    @GetMapping
    public HttpEntity<PersonResource> showAll() {
        return null;
    }

    @GetMapping("/{person}")
    public HttpEntity<PersonResource> show(final @PathVariable Long person) {
        return null;
    }
}
