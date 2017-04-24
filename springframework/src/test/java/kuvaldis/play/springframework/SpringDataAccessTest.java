package kuvaldis.play.springframework;

import kuvaldis.play.springframework.dataaccess.FooService;
import kuvaldis.play.springframework.dataaccess.InstrumentNotFoundException;
import kuvaldis.play.springframework.dataaccess.object.Actor;
import kuvaldis.play.springframework.dataaccess.object.ActorMappingQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;


public class SpringDataAccessTest {

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("test-data-access-context.xml");

    @After
    public void tearDown() throws Exception {
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.createStatement().execute("DELETE FROM test");
        connection.createStatement().execute("DELETE FROM actor");
        connection.close();
    }

    @Test
    public void testHelloWorld() throws Exception {
        // given
        // also DataSourceUtils might be used in order to get connection with transaction awareness
        final DataSource transactionAwareDataSource = context.getBean("transactionAwareDataSource", DataSource.class);
        final PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        final DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // when
        final TransactionStatus transaction1 = transactionManager.getTransaction(transactionDefinition);
        final Connection connection1 = transactionAwareDataSource.getConnection();

        connection1.createStatement().execute("INSERT INTO test VALUES ('bla 1')");
        connection1.createStatement().execute("INSERT INTO test VALUES ('bla 2')");
        final ResultSet resultSet1 = connection1.createStatement().executeQuery("select count(*) from test");
        resultSet1.next();
        final int count1 = resultSet1.getInt(1);

        transactionManager.rollback(transaction1);

        transactionManager.getTransaction(transactionDefinition);
        final Connection connection2 = transactionAwareDataSource.getConnection();
        final ResultSet resultSet2 = connection2.createStatement().executeQuery("select count(*) from test");
        resultSet2.next();
        final int count2 = resultSet2.getInt(1);

        // then
        assertEquals(2, count1);
        assertEquals(0, count2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRollbackOnRuntimeException() throws Exception {
        // given
        final FooService fooService = context.getBean("fooService", FooService.class);

        // when
        fooService.insertString("string");
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        final ResultSet resultSet2 = connection.createStatement().executeQuery("select count(*) from test");
        resultSet2.next();
        final int count = resultSet2.getInt(1);

        // then
        assertEquals(0, count);
    }

    @Test(expected = InstrumentNotFoundException.class)
    public void testNoRollbackOnUncheckedExceptionIfSpecified() throws Exception {
        // given
        final FooService fooService = context.getBean("fooService", FooService.class);

        // when
        fooService.insertInstrument("str");
        fooService.getInstrument();
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        final ResultSet resultSet2 = connection.createStatement().executeQuery("select data from test");
        resultSet2.next();
        final String instrument = resultSet2.getString(1);

        // then
        assertEquals("str", instrument);
    }

    @Test
    public void testMappingSqlQuery() throws Exception {
        // given
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);
        dataSource.getConnection().createStatement()
                .executeUpdate("INSERT INTO actor(id, first_name, last_name) VALUES (1, 'Vaas', 'Isdaas')");
        final ActorMappingQuery actorMappingQuery = new ActorMappingQuery(dataSource);

        // when
        final Actor actor = actorMappingQuery.findObject("1");

        // then
        assertEquals(1, actor.getId());
        assertEquals("Vaas", actor.getFirstName());
        assertEquals("Isdaas", actor.getLastName());
    }
}
