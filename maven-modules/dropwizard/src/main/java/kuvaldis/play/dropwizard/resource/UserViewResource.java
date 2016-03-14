package kuvaldis.play.dropwizard.resource;

import kuvaldis.play.dropwizard.domain.UserView;
import kuvaldis.play.dropwizard.service.UserViewService;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static kuvaldis.play.dropwizard.Constants.USER_ID_HEADER;

/**
 * Resource for /user-views endpoint
 */
@Path("/user-views")
@Produces(MediaType.APPLICATION_JSON)
public class UserViewResource {

    private final UserViewService userViewService;

    public UserViewResource(final UserViewService userViewService) {
        this.userViewService = userViewService;
    }

    /**
     * Current user views endpoint.
     *
     * @param currentUserId current user id
     * @return list of user views
     */
    @GET
    public List<UserView> getAll(final @HeaderParam(USER_ID_HEADER) Long currentUserId) {
        return userViewService.getUserViews(currentUserId);
    }
}
