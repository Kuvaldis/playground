package kuvaldis.play.springframework.dataaccess.object;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ActorMappingQuery extends MappingSqlQuery<Actor> {

    public ActorMappingQuery(final DataSource ds) {
        super(ds, "SELECT id, first_name, last_name FROM actor WHERE id = ?");
        super.declareParameter(new SqlParameter("id", Types.INTEGER));
        compile();
    }

    @Override
    protected Actor mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new Actor(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"));
    }
}
