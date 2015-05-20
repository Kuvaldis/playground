package kuvaldis.play.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandLineRunnerComponent implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // On start prints something like '<...>Running with first arg --spring.output.ansi.enabled=always'
        log.info("Running with first arg {}", args);
    }
}
