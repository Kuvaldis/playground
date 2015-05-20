package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingsService {

    @Value("${greetings.name}")
    private String name;

    @Value("${greetings.suffix}")
    private String suffix;

    public String greetings() {
        return String.format("Hello %s! %s", name, suffix);
    }
}
