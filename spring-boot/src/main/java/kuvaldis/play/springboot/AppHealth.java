package kuvaldis.play.springboot;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AppHealth implements HealthIndicator {
    @Override
    public Health health() {
        if (ThreadLocalRandom.current().nextInt(10) > 5) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}
