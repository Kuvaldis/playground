package kuvaldis.playground.liquibase;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class DbManager {

    private final String url;

    public DbManager() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        final String dbName = String.format("db%s", ThreadLocalRandom.current().nextLong());
        final String urlTemplate = "jdbc:h2:mem:${dbName}";
        this.url = urlTemplate.replaceAll("\\$\\{dbName}", dbName);
    }

    public Liquibase create(final String liquibaseFile) throws Exception {
        final Connection connection = getConnection();
        final Liquibase liquibase = new Liquibase(liquibaseFile, new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
        return liquibase;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, "sa", "");
    }

}
