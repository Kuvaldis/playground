package kuvaldis.play.dropwizard.filter;

import kuvaldis.play.dropwizard.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static kuvaldis.play.dropwizard.Constants.USER_ID_HEADER;

/**
 * User id checker filter. Added as a common user id parameter checker since user id parameter is required
 * in all the endpoints. It also validates whether the value is a correct long and if the user exists
 */
public class UserIdCheckerFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(UserIdCheckerFilter.class);

    private final UserService userService;

    public UserIdCheckerFilter(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Checks user id header correctness
     *
     * @param containerRequestContext request context
     * @throws IOException
     */
    public void filter(final ContainerRequestContext containerRequestContext) throws IOException {
        final Long userId = convertToId(containerRequestContext.getHeaderString(USER_ID_HEADER));
        if (userId == null) {
            containerRequestContext.abortWith(Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Missing or incorrect X-UserId header")
                    .build());
        } else if (!userService.userExists(userId)) {
            containerRequestContext.abortWith(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("X-UserId not found")
                    .build());
        }
    }

    /**
     * Method tries to convert raw user id to long value. In case the value is not a valid long, returns null
     *
     * @param userIdString user id raw value
     * @return null, if the value is not long. Otherwise long value
     */
    private Long convertToId(final String userIdString) {
        try {
            return Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            log.warn("Incorrect user id: {}", userIdString);
            return null;
        }
    }
}
