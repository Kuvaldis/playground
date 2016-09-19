package kuvaldis.play.java;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JDBC {

    @Before
    public void setUp() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "test", "");
        final Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS test");
        statement.executeUpdate("CREATE TABLE test(user VARCHAR(50), attrs ARRAY)");
    }

    @Test
    public void testConnectionWithSimplePreparedStatement() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "test", "");

        final Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO test(user) VALUES ('Bill')");
        statement.executeUpdate("INSERT INTO test(user) VALUES ('Cipher')");

        final ResultSet resultSet = statement.executeQuery("SELECT user FROM test");
        final List<String> users = new ArrayList<>();
        while (resultSet.next()) {
            final String name = resultSet.getString("user");
            users.add(name);
        }
        statement.close();

        assertEquals("Bill", users.get(0));
        assertEquals("Cipher", users.get(1));

        connection.close();
    }

    @Test
    public void testPreparedStatement() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "test", "");

        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test(user) VALUES (?)");
        preparedStatement.setString(1, "Bill");
        preparedStatement.execute();
        preparedStatement.setString(1, "Cipher");
        preparedStatement.execute();
        // or with batch
        preparedStatement.close();

        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT user FROM test");
        final List<String> users = new ArrayList<>();
        while (resultSet.next()) {
            final String name = resultSet.getString("user");
            users.add(name);
        }
        statement.close();

        assertEquals("Bill", users.get(0));
        assertEquals("Cipher", users.get(1));

        connection.close();
    }

    @Test
    public void testArray() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "test", "");

        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test(user, attrs) VALUES (?, ?)");
        // also connection.createArrayOf("varchar", new String[]{"1", "2"});, but doesn't work with h2
        preparedStatement.setString(1, "Bill Cipher");
        preparedStatement.setObject(2, new String[]{"yellow", "eye"});
        preparedStatement.execute();

        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT attrs FROM test");
        resultSet.next();
        // getArray in case arrays feature is supported
        final String[] attributes = (String[]) resultSet.getObject("attrs");
        assertEquals("yellow", attributes[0]);
        assertEquals("eye", attributes[1]);
        statement.close();

        preparedStatement.close();
        connection.close();
    }
}
