package kuvaldis.play.java;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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

    @Test
    public void testSemaphore() throws Exception {
        final Semaphore semaphore = new Semaphore(2);
        final AtomicInteger counter = new AtomicInteger();
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        final List<Future<?>> results = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            results.add(executorService.submit(() -> {
                try {
                    semaphore.acquire();
                    // here only 2 threads may be run
                    if (semaphore.availablePermits() > 1) {
                        // will never reach it as maximum 2 threads may acquire semaphore lock and one of them is acquired by current thread
                        counter.incrementAndGet();
                    }
                    // do some stuff
                    Thread.sleep(100);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }

        for (Future<?> result : results) {
            result.get();
        }

        assertEquals(0, counter.get());
    }

    @Test
    public void testCountDownLatch() throws Exception {
        final int threadsNumber = 5;
        final CountDownLatch latch = new CountDownLatch(threadsNumber);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < threadsNumber; i++) {
            new Thread(() -> {
                counter.incrementAndGet();
                latch.countDown();
            }).start();
        }
        // will wait until all threads call countDown()
        latch.await();
        assertEquals(5, counter.get());
    }

    @Test
    public void testCyclicBarrier() throws Exception {
        final int threadsNumber = 5;
        final CyclicBarrier barrier = new CyclicBarrier(threadsNumber + 1);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < threadsNumber; i++) {
            new Thread(() -> {
                try {
                    counter.incrementAndGet();
                    // will wait until other threads reach this place
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        // will wait for other threads either
        barrier.await();

        assertEquals(5, counter.get());
    }

    @Test
    public void testPhaser() throws Exception {
        final int threadsNumber = 5;
        final int[] values = new int[]{0, 0, 0, 0, 0};
        final Phaser phaser = new Phaser(threadsNumber) {

            // will be called when all threads arrived
            @Override
            protected boolean onAdvance(final int phase, final int registeredParties) {
                System.out.println(registeredParties);
                return IntStream.of(values).sum() == threadsNumber;
            }
        };

        class Run implements Runnable {

            private final int i;

            private Run(final int i) {
                this.i = i;
            }

            @Override
            public void run() {
                values[i] = 1;
                // will not wait for other threads
                phaser.arrive();
            }
        }

        // register current
        phaser.register();

        for (int i = 0; i < threadsNumber; i++) {
            new Thread(new Run(i)).start();
        }

        // will wait for other threads
        phaser.arriveAndAwaitAdvance();

        assertEquals(5, IntStream.of(values).sum());
    }
}
