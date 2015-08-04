package kuvaldis.play.jooq;

import org.h2.jdbcx.JdbcDataSource;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {

    public static void main(String[] args) throws Exception {
        final File dbFile = Paths.get("./library.mv.db").toFile();
        if (dbFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dbFile.delete();
        }
        final URL resource = DbUtils.class.getClassLoader().getResource("setup.sql");
        //noinspection ConstantConditions
        final String sql = new String(Files.readAllBytes(Paths.get(resource.toURI())), "UTF-8");
        final Connection connection = getConnection();
        final Statement statement = connection.createStatement();
        statement.execute(sql);
    }

    public static Connection getConnection() throws SQLException {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./library");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource.getConnection();
    }
}
