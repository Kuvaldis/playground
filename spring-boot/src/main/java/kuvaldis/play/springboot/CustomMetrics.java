package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.MetricReaderPublicMetrics;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomMetrics implements PublicMetrics {

    @Autowired
    private MetricReaderPublicMetrics metricReaderPublicMetrics;

    @Override
    public Collection<Metric<?>> metrics() {
        return Collections.singletonList(new Metric<>("random.long", ThreadLocalRandom.current().nextLong()));
    }
}
