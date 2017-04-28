package kuvaldis.play.springframework;

import kuvaldis.play.springframework.dataaccess.object.Actor;
import kuvaldis.play.springframework.orm.ActorDao;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


public class SpringOrmTest {

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("test-orm-context.xml");

    @After
    public void tearDown() throws Exception {
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.createStatement().execute("DELETE FROM actor");
        connection.close();
    }

    @Test
    public void testHelloWorld() throws Exception {
        // given
        final ActorDao actorDao = context.getBean("actorDao", ActorDao.class);

        // when
        final Collection<Actor> actors = actorDao.findByFirstName("Vasiliy");

        // then
        assertEquals(2, actors.size());
    }
}
