package kuvaldis.play.dropwizard.job;

import kuvaldis.play.dropwizard.service.UserViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserViewLimitJob implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(UserViewLimitJob.class);

    private final UserViewService userViewService;

    public UserViewLimitJob(final UserViewService userViewService) {
        this.userViewService = userViewService;
    }

    @Override
    public void run() {
        log.info("Execute cleanup user view over limit job");
        userViewService.deleteOverLimit();
    }
}
