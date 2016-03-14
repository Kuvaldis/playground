package kuvaldis.play.dropwizard.job;

import kuvaldis.play.dropwizard.service.UserViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupOldRecordsJob implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CleanupOldRecordsJob.class);

    private final UserViewService userViewService;

    public CleanupOldRecordsJob(final UserViewService userViewService) {

        this.userViewService = userViewService;
    }

    @Override
    public void run() {
        log.info("Execute cleanup old records job");
        userViewService.deleteOld();
    }
}
