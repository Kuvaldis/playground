package kuvaldis.play.dropwizard.service;

/**
 * Interface representing user service. Used in between REST API calls and DAO
 */
public interface UserService {

    /**
     * Inserts a bunch of users. Helper method to initialize data
     *
     * @param size size of the bulk
     */
    void insertUsers(Integer size);

    /**
     * Checks if the user with such id exists
     *
     * @param userId user id to check
     * @return true if user with given id exists
     */
    boolean userExists(Long userId);

}
