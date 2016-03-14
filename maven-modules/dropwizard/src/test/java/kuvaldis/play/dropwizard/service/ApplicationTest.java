package kuvaldis.play.dropwizard.service;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import kuvaldis.play.dropwizard.ApplicationConfiguration;
import kuvaldis.play.dropwizard.Main;
import kuvaldis.play.dropwizard.domain.UserView;
import org.junit.*;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Application API integration tests
 */
public class ApplicationTest {

    private static DBI DBI;
    private static Client CLIENT;

    @ClassRule
    public static final DropwizardAppRule<ApplicationConfiguration> RULE =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("application-test.yml"));


    @BeforeClass
    public static void setUpClass() throws Exception {
        // dbi object
        final DBIFactory factory = new DBIFactory();
        RULE.getConfiguration().getDataSourceFactory();
        DBI = factory.build(RULE.getEnvironment(), RULE.getConfiguration().getDataSourceFactory(), "h2");

        // client
        CLIENT = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

        // create test users
        Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("load users");
        client.target(String.format("http://localhost:%d/tasks/insert-users?size=20", RULE.getAdminPort()))
                .request()
                .post(Entity.text(""));
    }

    @After
    public void tearDown() throws Exception {
        final Handle handle = DBI.open();
        handle.createStatement("delete from user_view").execute();
    }

    @AfterClass
    public static void tearDownClass() {
        DBI.open().createStatement("drop all objects").execute();
    }

    @Test
    public void testMissingXUserIdShouldGive400Response() throws Exception {
        // given
        final Long viewUser = 2l;
        // when
        final Response response = CLIENT.target(String.format("http://localhost:%d/users/%s", RULE.getLocalPort(), viewUser))
                .request()
                .get();
        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testPresentXUserIdGives200Response() throws Exception {
        // given
        final Long currentUser = 1l;
        final Long viewUser = 2l;
        // when
        final Response response = CLIENT.target(String.format("http://localhost:%d/users/%s", RULE.getLocalPort(), viewUser))
                .request()
                .header("X-UserId", currentUser)
                .get();
        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testViewShouldCreateUserViewRecordWithCorrectDate() throws Exception {
        // given
        final Long currentUser = 1l;
        final Long viewUser = 2l;
        final LocalDateTime requestDateTime = LocalDateTime.now();
        // when
        CLIENT.target(String.format("http://localhost:%d/users/%s", RULE.getLocalPort(), viewUser))
                .request()
                .header("X-UserId", currentUser)
                .get();
        // then
        final List<Map<String, Object>> result = DBI.open()
                .select("select user_id, user_viewer_id, view_date_time from user_view");
        assertEquals(1, result.size());
        final Map<String, Object> entry = result.get(0);
        assertEquals(2l, entry.get("user_id"));
        assertEquals(1l, entry.get("user_viewer_id"));
        final Timestamp viewDateTime = (Timestamp) entry.get("view_date_time");
        assertTrue(requestDateTime.isBefore(viewDateTime.toLocalDateTime()));
        assertTrue(LocalDateTime.now().isAfter(viewDateTime.toLocalDateTime()));
    }

    @Test
    public void testOnly10RecordsShouldBeReturnedWhenThereAreMoreViews() throws Exception {
        // given
        final Handle handle = DBI.open();
        final long currentUser = 1l;
        final LocalDateTime startDate = LocalDateTime.now();
        for (long viewerId = 2l; viewerId < 20l; viewerId++) {
            final LocalDateTime viewLocalDateTime = startDate.withSecond((int) viewerId);
            final Date userViewDate = Date.from(viewLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
            handle.insert("insert into user_view (user_id, user_viewer_id, view_date_time) values(?, ?, ?)",
                    currentUser, viewerId, userViewDate);
        }
        // when
        final List<UserView> response = CLIENT.target(String.format("http://localhost:%d/user-views", RULE.getLocalPort()))
                .request()
                .header("X-UserId", currentUser)
                .get(new GenericType<List<UserView>>(){});
        // then
        assertEquals(10, response.size());
    }

    @Test
    public void testRecordsShouldNotBeLaterThen10Days() throws Exception {
        // given
        final Handle handle = DBI.open();
        final long currentUser = 1l;
        final long viewerUser = 2l;
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime tooLateDate = now.minusDays(10);
        final LocalDateTime correctDate = now.minusDays(1);
        handle.insert("insert into user_view (user_id, user_viewer_id, view_date_time) values(?, ?, ?)",
                currentUser, viewerUser, Date.from(tooLateDate.atZone(ZoneId.systemDefault()).toInstant()));
        handle.insert("insert into user_view (user_id, user_viewer_id, view_date_time) values(?, ?, ?)",
                currentUser, viewerUser, Date.from(correctDate.atZone(ZoneId.systemDefault()).toInstant()));
        // when
        final List<UserView> response = CLIENT.target(String.format("http://localhost:%d/user-views", RULE.getLocalPort()))
                .request()
                .header("X-UserId", currentUser)
                .get(new GenericType<List<UserView>>(){});
        // then
        assertEquals(1, response.size());
        assertEquals(Date.from(correctDate.atZone(ZoneId.systemDefault()).toInstant()), response.get(0).getViewDateTime());
    }
}
