package kuvaldis.play.springframework;

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


public class SpringDataAccessTest {

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("test-data-access-context.xml");

    @BeforeClass
    public static void setUp() throws Exception {
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.createStatement().execute("CREATE TABLE test (data VARCHAR(255))");
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
        final TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        final Connection connection = transactionAwareDataSource.getConnection();
        // todo do something
        transactionManager.commit(transaction);
    }
}
