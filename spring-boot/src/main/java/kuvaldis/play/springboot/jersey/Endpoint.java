package kuvaldis.play.springboot.jersey;

import kuvaldis.play.springboot.GreetingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Path("/jersey")
public class Endpoint {

    @Autowired
    private GreetingsService greetingsService;

//    @GET
    public String index() {
        return String.format("Jersey says: %s", greetingsService.greetings());
    }
}
