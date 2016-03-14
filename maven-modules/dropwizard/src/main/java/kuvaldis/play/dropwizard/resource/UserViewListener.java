package kuvaldis.play.dropwizard.resource;

/**
 * Interface used to call user view event listener
 */
public interface UserViewListener {

    /**
     * Listener method called when user is viewed
     *
     * @param userId       id representing a viewed user
     * @param userViewerId id representing a viewer user
     */
    void onView(final Long userId, final Long userViewerId);
}
