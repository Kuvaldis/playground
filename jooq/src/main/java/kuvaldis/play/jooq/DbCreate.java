package kuvaldis.play.jooq;

import org.h2.jdbcx.JdbcDataSource;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class DbCreate {

    public static void main(String[] args) throws Exception {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./library");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        final URL resource = DbCreate.class.getClassLoader().getResource("setup.sql");
        //noinspection ConstantConditions
        final String sql = new String(Files.readAllBytes(Paths.get(resource.toURI())), "UTF-8");
        final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute(sql);
    }
}
