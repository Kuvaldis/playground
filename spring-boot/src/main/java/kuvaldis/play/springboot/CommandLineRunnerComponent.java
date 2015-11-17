package kuvaldis.play.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerComponent implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CommandLineRunnerComponent.class);

    @Override
    public void run(String... args) throws Exception {
        // On start prints something like '<...>Running with first arg --spring.output.ansi.enabled=always'
        log.info("Running with first arg {}", args);
    }
}
