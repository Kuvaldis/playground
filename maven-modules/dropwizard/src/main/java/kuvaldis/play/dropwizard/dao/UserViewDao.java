package kuvaldis.play.dropwizard.dao;

import kuvaldis.play.dropwizard.domain.UserView;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Dao class to work with user views
 */
public interface UserViewDao {

    /**
     * Custom mapper to convert result set to user view
     */
    class UserViewMapper implements ResultSetMapper<UserView> {
        @Override
        public UserView map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
            return new UserView(r.getLong("USER_VIEWER_ID"), r.getTimestamp("VIEW_DATE_TIME"));
        }
    }

    /**
     * Inserts user view record to the database
     *
     * @param userId       id of the user
     * @param userViewerId id of the user viewer
     * @param viewDateTime date and time of the view event
     */
    @SqlUpdate("insert into USER_VIEW (USER_ID, USER_VIEWER_ID, VIEW_DATE_TIME) " +
            "values (:userId, :userViewerId, :viewDateTime)")
    void addViewer(final @Bind("userId") Long userId,
                   final @Bind("userViewerId") Long userViewerId,
                   final @Bind("viewDateTime") Date viewDateTime);

    /**
     * Retrieves no more than 10 records for the given user starting from the given date time
     * The records are not sorted in view date time order since there is no such a requirement
     *
     * @param userId           user id to retrieve views for
     * @param fromViewDateTime date and time to lookup views from
     * @return list of views
     */
    @RegisterMapper(UserViewMapper.class)
    @SqlQuery("select USER_VIEWER_ID, VIEW_DATE_TIME " +
            "from USER_VIEW " +
            "where USER_ID = :userId" +
            "  and VIEW_DATE_TIME > :fromViewDateTime " +
            "limit 10")
    List<UserView> getUserViews(final @Bind("userId") Long userId,
                                final @Bind("fromViewDateTime") Date fromViewDateTime);

    // the following methods are added to improve disk space consumption by removing user views

    /**
     * Method for cleaning up old rows
     *
     * @param fromViewDateTime oldest date
     */
    @SqlUpdate("delete from USER_VIEW " +
            "where VIEW_DATE_TIME <= :fromViewDateTime")
    void deleteOlderThan(final @Bind("fromViewDateTime") Date fromViewDateTime);

    /**
     * Experimental method to return users with views more than 30 as higher limit of stored view.
     * It is not 10 to prevent starvation, otherwise it might happen that only first 1000 users are retrieved every time.
     * Method might be slow with lots of data because of 'having',
     * maybe it is better not to use it. Or modify it requesting interval of ids.
     * It depends on the actual data.
     *
     * @return all users with views more than 30
     */
    @SqlQuery("select USER_ID " +
            "from USER_VIEW " +
            "group by USER_ID " +
            "having count(*) > 30 " +
            "limit 1000")
    List<Long> usersOverLimitViews();

    /**
     * Experimental method to delete all user view records except recent 10.
     *
     * @param userIds user ids to delete user views for
     */
    @SqlBatch("delete from USER_VIEW where USER_ID = :userId and VIEW_DATE_TIME < " +
            "  (select VIEW_DATE_TIME from " +
            "    (select VIEW_DATE_TIME from USER_VIEW where USER_ID = :userId order by VIEW_DATE_TIME desc limit 10) " +
            "     order by VIEW_DATE_TIME asc limit 1)")
    @BatchChunkSize(100)
    void deleteOverLimit(final @Bind("userId") List<Long> userIds);
}
