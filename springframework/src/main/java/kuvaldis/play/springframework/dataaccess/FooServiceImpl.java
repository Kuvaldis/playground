package kuvaldis.play.springframework.dataaccess;

import javax.sql.DataSource;
import java.sql.SQLException;

public class FooServiceImpl implements FooService {

    private final DataSource dataSource;

    public FooServiceImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertString(final String string) {
        try {
            dataSource.getConnection().createStatement().execute("INSERT INTO test VALUES ('bla')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new UnsupportedOperationException();
    }
}
