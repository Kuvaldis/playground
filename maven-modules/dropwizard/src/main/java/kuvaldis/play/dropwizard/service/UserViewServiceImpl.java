package kuvaldis.play.dropwizard.service;

import kuvaldis.play.dropwizard.dao.UserViewDao;
import kuvaldis.play.dropwizard.domain.UserView;
import kuvaldis.play.dropwizard.resource.UserViewListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default user view service implementation. Also logs all view events
 */
public class UserViewServiceImpl implements UserViewService, UserViewListener {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final UserViewDao userViewDao;

    public UserViewServiceImpl(final UserViewDao userViewDao) {
        this.userViewDao = userViewDao;
    }

    /**
     * Logs user view event. Done asynchronously, but in case of exceptions the data will not be written
     *
     * @param userId       id representing a viewed user
     * @param userViewerId id representing a viewer user
     */
    @Override
    public void onView(final Long userId, final Long userViewerId) {
        final Date currentDate = new Date();
        executorService.submit(() -> userViewDao.addViewer(userId, userViewerId, currentDate));
    }

    /**
     * {@inheritDoc}
     * Returns no more than 10 records not older than 10 days
     */
    @Override
    public List<UserView> getUserViews(final Long userId) {
        final Date fromDateTime = getFromDate();
        return userViewDao.getUserViews(userId, fromDateTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOld() {
        userViewDao.deleteOlderThan(getFromDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOverLimit() {
        // might be slow, so maybe it's better just to scroll over users and call deleteOverLimit
        final List<Long> userIds = userViewDao.usersOverLimitViews();
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        userViewDao.deleteOverLimit(userIds);
    }

    /**
     * Returns 10 days before the current date
     * @return date
     */
    private Date getFromDate() {
        final LocalDateTime localDateTime = LocalDateTime.now().minusDays(10);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
