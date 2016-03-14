package kuvaldis.play.dropwizard.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Job to run old records cleanup
 */
public class FixedIntervalJobFactory {

    private static final Logger log = LoggerFactory.getLogger(FixedIntervalJobFactory.class);

    private final ScheduledExecutorService scheduler;

    public FixedIntervalJobFactory(final int threadsNumber) {
        log.info("Create scheduler with threads number {}", threadsNumber);
        this.scheduler = Executors.newScheduledThreadPool(threadsNumber);
    }

    /**
     * Should be run to initialize jobs
     *
     * @param jobInterval job performing interval
     * @param runnable    actual job to perform
     */
    public void init(final int jobInterval, final Runnable runnable) {
        log.info("Init with interval {}", jobInterval);
        scheduler.scheduleAtFixedRate(runnable, 0l, jobInterval, TimeUnit.SECONDS);
    }
}
