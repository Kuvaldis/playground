package kuvaldis.play.dropwizard.task;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import kuvaldis.play.dropwizard.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

/**
 * Additional admin task to create a bunch of users
 */
public class InsertUsersTask extends Task {

    private static final Logger log = LoggerFactory.getLogger(InsertUsersTask.class);

    private static final String SIZE_PARAM = "size";

    /**
     * Default size of inserted users
     */
    private static final Integer DEFAULT_SIZE = 1000;

    private final UserService userService;

    public InsertUsersTask(final UserService userService) {
        super("insert-users");
        this.userService = userService;
    }

    @Override
    public void execute(final ImmutableMultimap<String, String> parameters, final PrintWriter output) throws Exception {
        final Integer size = getSize(parameters);
        userService.insertUsers(size);
    }

    /**
     * Retrieves size param from request params
     * @param parameters request params
     * @return size from param or default
     */
    private Integer getSize(final ImmutableMultimap<String, String> parameters) {
        final ImmutableCollection<String> paramValue = parameters.get(SIZE_PARAM);
        if (paramValue == null) {
            return DEFAULT_SIZE;
        }
        try {
            return paramValue.stream()
                    .findFirst()
                    .map(Integer::valueOf)
                    .orElse(DEFAULT_SIZE);
        } catch (NumberFormatException e) {
            log.error("Incorrect size");
            return DEFAULT_SIZE;
        }
    }


}
