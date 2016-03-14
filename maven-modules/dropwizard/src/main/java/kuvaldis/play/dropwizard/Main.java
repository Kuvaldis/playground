package kuvaldis.play.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import kuvaldis.play.dropwizard.dao.UserDao;
import kuvaldis.play.dropwizard.dao.UserViewDao;
import kuvaldis.play.dropwizard.filter.UserIdCheckerFilter;
import kuvaldis.play.dropwizard.job.CleanupOldRecordsJob;
import kuvaldis.play.dropwizard.job.FixedIntervalJobFactory;
import kuvaldis.play.dropwizard.job.UserViewLimitJob;
import kuvaldis.play.dropwizard.resource.UserResource;
import kuvaldis.play.dropwizard.resource.UserViewResource;
import kuvaldis.play.dropwizard.service.UserService;
import kuvaldis.play.dropwizard.service.UserServiceImpl;
import kuvaldis.play.dropwizard.service.UserViewService;
import kuvaldis.play.dropwizard.service.UserViewServiceImpl;
import kuvaldis.play.dropwizard.task.InsertUsersTask;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.Collections;

public class Main extends Application<ApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "Hello Dropwizard";
    }

    @Override
    public void run(final ApplicationConfiguration applicationConfiguration, final Environment environment) throws Exception {
        // jdbi
        final DBI jdbi = createJdbi(applicationConfiguration, environment);

        // run migration
        Handle handle = jdbi.open();
        Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(handle.getConnection()));
        liquibase.update("");

        // dao
        final UserDao userDao = createUserDao(jdbi);
        final UserViewDao userViewDao = createUserViewDao(jdbi);

        // service
        final UserService userService = new UserServiceImpl(userDao);
        final UserViewServiceImpl userViewService = new UserViewServiceImpl(userViewDao);

        // resources
        final UserResource userResource = new UserResource(userService);
        userResource.setUserViewListeners(Collections.singletonList(userViewService));
        environment.jersey().register(userResource);
        final UserViewResource userViewResource = new UserViewResource(userViewService);
        environment.jersey().register(userViewResource);

        // admin resources
        environment.admin().addTask(new InsertUsersTask(userService));

        // filters
        environment.jersey().register(new UserIdCheckerFilter(userService));

        // jobs
        registerCleanupJobs(userViewService, applicationConfiguration);
    }

    private void registerCleanupJobs(final UserViewService userViewService, final ApplicationConfiguration applicationConfiguration) {
        if (applicationConfiguration.isCleanupOldRecordsJobEnabled()) {
            new FixedIntervalJobFactory(applicationConfiguration.getCleanupOldRecordsJobThreads())
                    .init(applicationConfiguration.getCleanupOldRecordsJobInterval(), new CleanupOldRecordsJob(userViewService));
        }
        if (applicationConfiguration.isCleanupUserViewLimitJobEnabled()) {
            new FixedIntervalJobFactory(applicationConfiguration.getCleanupUserViewLimitJobThreads())
                    .init(applicationConfiguration.getCleanupUserViewLimitJobInterval(), new UserViewLimitJob(userViewService));
        }
    }

    private DBI createJdbi(final ApplicationConfiguration applicationConfiguration, final Environment environment) {
        final DBIFactory factory = new DBIFactory();
        return factory.build(environment, applicationConfiguration.getDataSourceFactory(), "h2");
    }

    private UserViewDao createUserViewDao(final DBI jdbi) {
        return jdbi.onDemand(UserViewDao.class);
    }

    private UserDao createUserDao(final DBI jdbi) {
        return jdbi.onDemand(UserDao.class);
    }
}
