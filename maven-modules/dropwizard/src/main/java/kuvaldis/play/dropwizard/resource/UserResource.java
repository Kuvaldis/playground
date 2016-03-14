package kuvaldis.play.dropwizard.resource;

import kuvaldis.play.dropwizard.domain.User;
import kuvaldis.play.dropwizard.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import static kuvaldis.play.dropwizard.Constants.USER_ID_HEADER;

/**
 * Resource for /users endpoint
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    private List<UserViewListener> userViewListeners = new ArrayList<>();

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    /**
     * View user endpoint. Returns user data
     *
     * @param currentUserId current user id
     * @param userId        user id to get the data about
     * @return user data for userId
     */
    @GET
    @Path("/{userId}")
    public User get(final @HeaderParam(USER_ID_HEADER) Long currentUserId,
                    final @PathParam("userId") Long userId,
                    final @Context HttpServletResponse response) {
        if (!userService.userExists(userId)) {
            throw new NotFoundException();
        }
        for (UserViewListener userViewListener : userViewListeners) {
            userViewListener.onView(userId, currentUserId);
        }
        return new User(userId);
    }

    public void setUserViewListeners(final List<UserViewListener> userViewListeners) {
        this.userViewListeners = userViewListeners;
    }
}
