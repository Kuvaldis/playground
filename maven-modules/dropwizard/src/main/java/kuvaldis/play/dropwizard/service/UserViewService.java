package kuvaldis.play.dropwizard.service;

import kuvaldis.play.dropwizard.domain.UserView;

import java.util.List;

/**
 * Interface representing user view service. Used in between REST API calls and DAO
 */
public interface UserViewService {

    /**
     * Returns user views
     * @param userId user id to return users for
     * @return list of user views
     */
    List<UserView> getUserViews(Long userId);

    /**
     * Method for removing old data
     */
    void deleteOld();

    /**
     * Method for removing user view records over limit of 10 per user
     */
    void deleteOverLimit();
}
