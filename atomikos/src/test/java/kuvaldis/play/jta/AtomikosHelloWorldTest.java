package kuvaldis.play.jta;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AtomikosHelloWorldTest {

    @Test
    public void testJtaCommit() throws Exception {
        final UserTransaction transaction = new UserTransactionImp();
        transaction.begin();
        final DataSource dataSource = getNonXaDataSource();
        final Connection connection = dataSource.getConnection();
        connection.createStatement().execute("INSERT INTO TEST VALUES(1)");
        connection.createStatement().execute("INSERT INTO TEST VALUES(2)");
        connection.close();
        transaction.commit();
        final ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM TEST");
        assertTrue(resultSet.next());
        assertEquals(1, resultSet.getInt(1));
        assertTrue(resultSet.next());
        assertEquals(2, resultSet.getInt(1));
        assertFalse(resultSet.next());
    }

    @Test
    public void testJtaRollback() throws Exception {
        final UserTransaction transaction = new UserTransactionImp();
        transaction.begin();
        final DataSource dataSource = getNonXaDataSource();
        final Connection connection = dataSource.getConnection();
        connection.createStatement().execute("INSERT INTO TEST VALUES(1)");
        connection.createStatement().execute("INSERT INTO TEST VALUES(2)");
        connection.close();
        transaction.rollback();
        final ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM TEST");
        assertFalse(resultSet.next());
    }

    private DataSource getNonXaDataSource() throws SQLException {
        final AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUniqueResourceName("test");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE TEST(TEST INT)");
        return dataSource;
    }

    @Test
    public void testXaCommit() throws Exception {
        final XADataSource dataSource1 = getXaDataSource("1");
        final XADataSource dataSource2 = getXaDataSource("2");
        final TransactionManager transactionManager = new UserTransactionManager();
        transactionManager.begin();
        final XAConnection connection1 = dataSource1.getXAConnection();
        final XAConnection connection2 = dataSource2.getXAConnection();
        final XAResource resource1 = connection1.getXAResource();
        final XAResource resource2 = connection2.getXAResource();
        final Transaction transaction = transactionManager.getTransaction();
        transaction.enlistResource(resource1);
        transaction.enlistResource(resource2);
        connection1.getConnection().createStatement().executeUpdate("INSERT INTO TEST VALUES(1)");
        connection2.getConnection().createStatement().executeUpdate("INSERT INTO TEST VALUES(2)");
        transaction.delistResource(resource1, XAResource.TMSUCCESS);
        transaction.delistResource(resource2, XAResource.TMSUCCESS);
        transactionManager.commit();
        final ResultSet resultSet1 = connection1.getConnection().createStatement().executeQuery("SELECT * FROM TEST");
        final ResultSet resultSet2 = connection2.getConnection().createStatement().executeQuery("SELECT * FROM TEST");
        assertTrue(resultSet1.next());
        assertTrue(resultSet2.next());
        assertEquals(1, resultSet1.getInt(1));
        assertEquals(2, resultSet2.getInt(1));
        assertFalse(resultSet1.next());
        assertFalse(resultSet2.next());
        connection1.getConnection().close();
        connection2.getConnection().close();
        connection1.close();
        connection2.close();
    }

    @Test
    public void testXaRollback() throws Exception {
        final XADataSource dataSource1 = getXaDataSource("1");
        final XADataSource dataSource2 = getXaDataSource("2");
        final TransactionManager transactionManager = new UserTransactionManager();
        transactionManager.begin();
        final XAConnection connection1 = dataSource1.getXAConnection();
        final XAConnection connection2 = dataSource2.getXAConnection();
        final XAResource resource1 = connection1.getXAResource();
        final XAResource resource2 = connection2.getXAResource();
        final Transaction transaction = transactionManager.getTransaction();
        transaction.enlistResource(resource1);
        transaction.enlistResource(resource2);
        connection1.getConnection().createStatement().executeUpdate("INSERT INTO TEST VALUES(1)");
        connection2.getConnection().createStatement().executeUpdate("INSERT INTO TEST VALUES(2)");
        transaction.delistResource(resource1, XAResource.TMSUCCESS);
        transaction.delistResource(resource2, XAResource.TMFAIL);
        transactionManager.rollback();
        final ResultSet resultSet1 = connection1.getConnection().createStatement().executeQuery("SELECT * FROM TEST");
        final ResultSet resultSet2 = connection2.getConnection().createStatement().executeQuery("SELECT * FROM TEST");
        assertFalse(resultSet1.next());
        assertFalse(resultSet2.next());
        connection1.getConnection().close();
        connection2.getConnection().close();
        connection1.close();
        connection2.close();
    }

    private XADataSource getXaDataSource(final String suffix) throws SQLException {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test" + suffix);
        dataSource.setUser("sa");
        dataSource.setPassword("");
        final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE TEST(TEST INT)");
        return dataSource;
    }
}
