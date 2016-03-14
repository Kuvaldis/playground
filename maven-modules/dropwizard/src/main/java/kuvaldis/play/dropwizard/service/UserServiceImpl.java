package kuvaldis.play.dropwizard.service;

import kuvaldis.play.dropwizard.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Default user service implementation
 */
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    /**
     * Field holding user id.
     * With auto increment id in order to check whether or not a user with given id exists
     * we need only to know max user id
     */
    private volatile long max;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
        try {
            lock.writeLock().lock();
            this.max = userDao.max();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUsers(final Integer size) {
        log.info("Create {} users", size);
        // lock to update max user id
        try {
            lock.writeLock().lock();
            userDao.insertUsers(IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()));
            this.max = userDao.max();
        } finally {
            lock.writeLock().unlock();
        }
        log.info("{} users are inserted. Max id is {}", size, max);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean userExists(final Long userId) {
        try {
            lock.readLock().lock();
            return userId <= max;
        } finally {
            lock.readLock().unlock();
        }
    }
}
