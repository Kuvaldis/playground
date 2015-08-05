package kuvaldis.play.jooq;

import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

public final class DbUtils {

    private DbUtils() {
    }

    public static Connection connection() throws SQLException {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:../library");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource.getConnection();
    }

    public static DSLContext context() throws SQLException {
        return DSL.using(connection(), SQLDialect.H2);
    }
}
