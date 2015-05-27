package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Transactional
public class IndexController {

    @Autowired
    private ImportantConfig importantConfig;

    @Autowired
    private GreetingsService greetingsService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/")
    String home() {
        return greetingsService.greetings();
    }

    @RequestMapping("/important")
    List<String> important() {
        return importantConfig.getStuff();
    }

    @RequestMapping("/people")
    List<Person> people() {
        counterService.increment("index.controller.people.hit");
        return personRepository.findAll();
    }

    @RequestMapping("/people/add")
    List<Person> addPerson() {
        personRepository.save(new Person(UUID.randomUUID().toString()));
        return people();
    }

    @RequestMapping(value = "/login")
    void login(final @RequestParam String username, final @RequestParam String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/api/secret")
    public String secret() {
        return "No secret here";
    }

    @RequestMapping("/404")
    String error404() {
        return "404 Not found";
    }
}
