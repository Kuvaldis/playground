package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/404")
    String error404() {
        return "404 Not found";
    }
}
