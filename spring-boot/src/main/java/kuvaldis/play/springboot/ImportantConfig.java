package kuvaldis.play.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "important")
public class ImportantConfig {

    private List<String> stuff = new ArrayList<>();

    // called to add values for properties list important.stuff[0], [1] etc.
    public List<String> getStuff() {
        return stuff;
    }
}
