package kuvaldis.play.dropwizard.dao;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import kuvaldis.play.dropwizard.ApplicationConfiguration;
import kuvaldis.play.dropwizard.Main;
import kuvaldis.play.dropwizard.domain.UserView;
import org.junit.*;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserViewDaoTest {

    private static DBI DBI;

    private UserViewDao userViewDao;

    @ClassRule
    public static final DropwizardAppRule<ApplicationConfiguration> RULE =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("application-test.yml"));

    @BeforeClass
    public static void setUpClass() throws Exception {
        // dbi object
        final DBIFactory factory = new DBIFactory();
        RULE.getConfiguration().getDataSourceFactory();
        DBI = factory.build(RULE.getEnvironment(), RULE.getConfiguration().getDataSourceFactory(), "h2");
    }

    @Before
    public void setUp() throws Exception {
        userViewDao = DBI.onDemand(UserViewDao.class);
    }

    @After
    public void tearDown() throws Exception {
        final Handle handle = DBI.open();
        handle.createStatement("delete from user_view").execute();
    }

    @Test
    public void testAddUserViewShouldAddCorrectRecordToDatabase() throws Exception {
        // given
        final Long userId = 1l;
        final Long userViewerId = 2l;
        final Date viewDateTime = new Date();
        // when
        userViewDao.addViewer(userId, userViewerId, viewDateTime);
        // then
        final List<Map<String, Object>> result = DBI.open()
                .select("select user_id, user_viewer_id, view_date_time from user_view");
        assertEquals(1, result.size());
        final Map<String, Object> entry = result.get(0);
        assertEquals(1l, entry.get("user_id"));
        assertEquals(2l, entry.get("user_viewer_id"));
        final Timestamp viewDateTimestamp = (Timestamp) entry.get("view_date_time");
        assertEquals(viewDateTime, viewDateTimestamp);
    }

    @Test
    public void testGetUserViewShouldNotReturnOldRecords() throws Exception {
        // given
        final Long userId = 1l;
        final Long userViewerId = 2l;
        final Date correctDate = new Date();
        final Date fromDate = Date.from(LocalDateTime.now().minusDays(10).atZone(ZoneId.systemDefault()).toInstant());
        final Date oldDate = Date.from(LocalDateTime.now().minusDays(11).atZone(ZoneId.systemDefault()).toInstant());
        final Handle handle = DBI.open();
        handle.insert("insert into user_view (user_id, user_viewer_id, view_date_time) values(?, ?, ?)",
                userId, userViewerId, correctDate);
        handle.insert("insert into user_view (user_id, user_viewer_id, view_date_time) values(?, ?, ?)",
                userId, userViewerId, oldDate);
        // when
        final List<UserView> userViews = userViewDao.getUserViews(userId, fromDate);
        // then
        assertEquals(1, userViews.size());
        assertEquals(userViewerId, userViews.get(0).getUserViewerId());
        assertEquals(correctDate, userViews.get(0).getViewDateTime());
    }

    @Test
    public void testGetUserViewShouldNotReturnMoreThan10Records() throws Exception {
        // given
        final Long userId = 1l;
        final Date fromDate = Date.from(LocalDateTime.now().minusDays(20).atZone(ZoneId.systemDefault()).toInstant());
        final Handle handle = DBI.open();
        for (int i = 1; i < 20; i++) {
            handle.insert("insert into user_view (user_id, user_viewer_id, view_date_time) values(?, ?, ?)",
                    userId, i, new Date());
        }
        // when
        final List<UserView> userViews = userViewDao.getUserViews(userId, fromDate);
        // then
        assertEquals(10, userViews.size());
    }

}