package kuvaldis.play.hateoas;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/hateoas")
public class HateoasTestController {

    @GetMapping("ping")
    public String ping() {
        return "pong";
    }
}
