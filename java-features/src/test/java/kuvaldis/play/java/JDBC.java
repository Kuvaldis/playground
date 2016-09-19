package kuvaldis.play.java;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
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
        statement.executeUpdate("CREATE TABLE test(user VARCHAR(50), attrs ARRAY, data CLOB, s VARCHAR(50))");
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
        preparedStatement.close();

        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT attrs FROM test");
        resultSet.next();
        // getArray in case arrays feature is supported
        final String[] attributes = (String[]) resultSet.getObject("attrs");
        assertEquals("yellow", attributes[0]);
        assertEquals("eye", attributes[1]);
        statement.close();

        connection.close();
    }

    @Test
    public void testClob() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "test", "");
        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test(data) VALUES (?)");

        preparedStatement.setClob(1, new StringReader("big data"));
        preparedStatement.execute();
        preparedStatement.close();

        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT data FROM test");
        resultSet.next();
        final BufferedReader characterStream = new BufferedReader(resultSet.getCharacterStream(1));
        final String readString = characterStream.readLine();
        assertEquals("big data", readString);
        statement.close();

        connection.close();
    }

    @Test
    public void testTransactionRollback() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "test", "");
        connection.setAutoCommit(false);
        final Statement statement = connection.createStatement();
        statement.execute("INSERT INTO test(user, s) VALUES ('Bill', 'String 1')");
        statement.close();
        connection.commit();

        final Statement statement1 = connection.createStatement();
        final ResultSet resultSet1 = statement1.executeQuery("SELECT s FROM test WHERE user = 'Bill'");
        resultSet1.next();
        assertEquals("String 1", resultSet1.getString(1));
        statement1.close();

        final Savepoint savepoint = connection.setSavepoint();

        final Statement statement2 = connection.createStatement();
        statement2.executeUpdate("UPDATE test SET s = 'String 2' WHERE user = 'Bill'");
        statement2.close();

        final Statement statement3 = connection.createStatement();
        final ResultSet resultSet2 = statement3.executeQuery("SELECT s FROM test WHERE user = 'Bill'");
        resultSet2.next();
        assertEquals("String 2", resultSet2.getString(1));
        statement3.close();

        connection.rollback(savepoint);

        final Statement statement4 = connection.createStatement();
        final ResultSet resultSet3 = statement4.executeQuery("SELECT s FROM test WHERE user = 'Bill'");
        resultSet3.next();
        assertEquals("String 1", resultSet3.getString(1));
        statement4.close();
    }
}
