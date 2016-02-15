package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingsService {

//    @Value("${greetings.name}")
//    private String name;

    @Value("${greetings.suffix}")
    private String suffix;

    // change even method signatures, then in Intellij Idea recompile, it'll reload application,
    // but with optimization. Better use Spring boot plugin
    public String greetings() {
        return returnString();
    }

    private String returnString() {
        return "aaa";
    }
}
