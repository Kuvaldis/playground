package kuvaldis.play.jta;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractTest {

    protected DataSource getDataSource1() throws SQLException {
        return getDataSource("1");
    }

    protected DataSource getDataSource2() throws SQLException {
        return getDataSource("2");
    }

    private DataSource getDataSource(final String suffix) throws SQLException {
        final AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUniqueResourceName("test" + suffix);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test" + suffix);
        dataSource.setUser("sa");
        dataSource.setPassword("");
        final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE TEST(TEST INT)");
        return dataSource;
    }

}
