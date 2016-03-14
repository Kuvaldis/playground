package kuvaldis.play.dropwizard.dao;

import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;

import java.util.List;

/**
 * Dao class to work with users
 */
public interface UserDao {

    /**
     * Additional method to insert users. Just for convenience and does not affect main logic
     * @param collect this is a dummy param only to define how many users should be inserted
     */
    @SqlBatch("insert into user default values")
    @BatchChunkSize(10000)
    void insertUsers(final List<Integer> collect);

    /**
     * Max user id in the database
     * @return max user id
     */
    @SqlQuery("select max(id) from user")
    long max();
}
