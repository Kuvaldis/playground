package kuvaldis.play.java;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class Multithreading {

    @Test
    public void testScheduledExecutor() throws Exception {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final AtomicInteger counter = new AtomicInteger();
        final ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(counter::incrementAndGet, 0, 2, SECONDS);
        scheduler.schedule(() -> scheduledFuture.cancel(true), 3, SECONDS);
        Thread.sleep(3000);
        assertEquals(2, counter.get());
    }

    @Test
    public void testExchanger() throws Exception {
        final Exchanger<String> exchanger = new Exchanger<>();
        final AtomicInteger counter = new AtomicInteger();
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread producer = new Thread(() -> {
            try {
                exchanger.exchange("Produce");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread consumer = new Thread(() -> {
            final String consume;
            try {
                consume = exchanger.exchange("Consume");
                if ("Produce".equals(consume)) {
                    counter.incrementAndGet();
                    latch.countDown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();

        latch.await(1, TimeUnit.SECONDS);

        assertEquals(1, counter.get());
    }
}
