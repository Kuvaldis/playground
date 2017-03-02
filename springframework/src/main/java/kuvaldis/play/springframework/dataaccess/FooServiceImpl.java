package kuvaldis.play.springframework.dataaccess;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
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
        doInsertString(string);
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInstrument() {
        throw new InstrumentNotFoundException();
    }

    @Override
    public void insertInstrument(final String instrument) {
        doInsertString(instrument);
    }

    private void doInsertString(final String instrument) {
        try {
            final PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("INSERT INTO test VALUES (?)");
            preparedStatement.setString(1, instrument);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
