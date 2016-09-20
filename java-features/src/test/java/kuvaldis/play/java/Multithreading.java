package kuvaldis.play.java;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Multithreading {

    @Test
    public void testScheduledExecutor() throws Exception {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final AtomicInteger counter = new AtomicInteger();
        final ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(counter::incrementAndGet, 0, 2, SECONDS);
        scheduler.schedule(() -> scheduledFuture.cancel(true), 3, SECONDS);
        Thread.sleep(3000);
        Assert.assertEquals(2, counter.get());
    }
}
