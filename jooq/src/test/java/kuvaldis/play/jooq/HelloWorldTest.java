package kuvaldis.play.jooq;

import org.junit.Test;

import javax.sql.DataSource;

public class HelloWorldTest extends AbstractTest {

    @Test
    public void testName() throws Exception {
        final DataSource dataSource = getDataSource();
    }
}
