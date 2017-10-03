package kuvaldis.playground.liquibase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiquibaseTest {

    private DbManager manager;

    @Before
    public void setUp() throws Exception {
        this.manager = new DbManager();
    }

    @Test
    public void testPing() throws Exception {
        manager.init("liquibase-ping.xml");
        final Connection connection = manager.getConnection();
        final ResultSet countRS = connection.createStatement().executeQuery("SELECT count(*) FROM ping");
        countRS.first();
        final long count = countRS.getLong(1);
        assertEquals(1L, count);
        final ResultSet resultSet = connection.createStatement()
                .executeQuery("SELECT ping FROM ping");
        resultSet.first();
        final String ping = resultSet.getString("ping");
        assertEquals("pong", ping);
    }
}
