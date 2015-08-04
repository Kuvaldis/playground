package kuvaldis.play.jooq;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractTest {

    protected DataSource getDataSource() throws Exception {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        final URL resource = AbstractTest.class.getClassLoader().getResource("setup.sql");
        //noinspection ConstantConditions
        final String sql = new String(Files.readAllBytes(Paths.get(resource.toURI())), "UTF-8");
        final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute(sql);
        return dataSource;
    }
}
