package kuvaldis.play.jta;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.junit.Test;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AtomikosHelloWorldTest extends AbstractTest {

    @Test
    public void testJtaCommit() throws Exception {
        final UserTransaction transaction = new UserTransactionImp();
        transaction.begin();
        final DataSource dataSource = getDataSource1();
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
    }

    @Test
    public void testJtaRollback() throws Exception {
        final UserTransaction transaction = new UserTransactionImp();
        transaction.begin();
        final DataSource dataSource = getDataSource1();
        final Connection connection = dataSource.getConnection();
        connection.createStatement().execute("INSERT INTO TEST VALUES(1)");
        connection.createStatement().execute("INSERT INTO TEST VALUES(2)");
        connection.close();
        transaction.rollback();
        final ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM TEST");
        assertFalse(resultSet.next());
    }
}
