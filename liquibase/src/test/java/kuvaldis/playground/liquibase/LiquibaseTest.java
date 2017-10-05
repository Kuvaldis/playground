package kuvaldis.playground.liquibase;

import liquibase.Liquibase;
import liquibase.exception.MigrationFailedException;
import liquibase.exception.PreconditionFailedException;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiquibaseTest {

    private DbManager manager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.manager = new DbManager();
    }

    @Test
    public void testPing() throws Exception {
        final Liquibase liquibase = manager.create("liquibase-ping.xml");
        liquibase.update("");
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

    @Test
    public void testContext() throws Exception {
        final Liquibase liquibase = manager.create("liquibase-context.xml");
        liquibase.update("billy");
        final Connection connection = manager.getConnection();
        final ResultSet countRS = connection.createStatement().executeQuery("SELECT count(*) FROM person");
        countRS.first();
        final long count = countRS.getLong(1);
        assertEquals(1L, count);
        final ResultSet resultSet = connection.createStatement()
                .executeQuery("SELECT name FROM person");
        resultSet.first();
        final String billy = resultSet.getString("name");
        assertEquals("billy", billy);
    }

    @Test
    public void testPreconditions() throws Exception {
        final Liquibase liquibase = manager.create("liquibase-preconditions.xml");
        liquibase.update("create");
        final Connection connection = manager.getConnection();
        connection.createStatement()
                .executeUpdate("INSERT INTO person (name) VALUES ('webby')");
        expectedException.expect(MigrationFailedException.class);
        expectedException.expectCause(CoreMatchers.isA(PreconditionFailedException.class));
        liquibase.update("billy");
    }

    @Test
    public void testDrop() throws Exception {
        final Connection connection1 = manager.getConnection();
        connection1.createStatement().execute("CREATE TABLE person (name VARCHAR (255))");
        final Liquibase liquibase = manager.create("liquibase-rollback.xml");
        liquibase.update("");
        final Connection connection2 = manager.getConnection();
        final ResultSet countRS = connection2.createStatement().executeQuery("SELECT count(*) FROM person");
        countRS.first();
        final long count = countRS.getLong(1);
        assertEquals(1L, count);
        liquibase.rollback(1, "");
        final Connection connection3 = manager.getConnection();
        final ResultSet countRS2 = connection3.createStatement().executeQuery("SELECT count(*) FROM person");
        countRS2.first();
        final long count2 = countRS2.getLong(1);
        assertEquals(0L, count2);
    }
}

