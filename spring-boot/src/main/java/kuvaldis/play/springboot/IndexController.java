package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private ImportantConfig importantConfig;

    @Autowired
    private GreetingsService greetingsService;

    @RequestMapping("/")
    String home() {
        return greetingsService.greetings();
    }

    @RequestMapping("/important")
    List<String> important() {
        return importantConfig.getStuff();
    }

}
