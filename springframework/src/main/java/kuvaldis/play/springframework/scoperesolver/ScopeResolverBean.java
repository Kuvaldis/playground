package kuvaldis.play.springframework.scoperesolver;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScopeResolverBean {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int count;

    public ScopeResolverBean() {
        this.count = COUNTER.incrementAndGet();
    }

    public int getCount() {
        return count;
    }
}
