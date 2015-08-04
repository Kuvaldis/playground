package kuvaldis.play.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.Connection;

import static junit.framework.Assert.assertEquals;
import static kuvaldis.play.jooq.generated.public_.tables.Author.AUTHOR;

public class HelloWorldTest {

    @Test
    public void testAuthorsList() throws Exception {
        final Connection connection = DbUtils.getConnection();
        final DSLContext create = DSL.using(connection, SQLDialect.H2);
        final Result<Record> result = create.select().from(AUTHOR).fetch();

        System.out.println(result);

        assertEquals(3, result.size());

        final Record firstRecord = result.get(0);
        assertEquals(1, firstRecord.getValue(AUTHOR.ID).intValue());
        assertEquals("Howard", firstRecord.getValue(AUTHOR.FIRST_NAME));
        assertEquals("Lovecraft", firstRecord.getValue(AUTHOR.LAST_NAME));

        final Record secondRecord = result.get(1);
        assertEquals(2, secondRecord.getValue(AUTHOR.ID).intValue());
        assertEquals("Mark", secondRecord.getValue(AUTHOR.FIRST_NAME));
        assertEquals("Twain", secondRecord.getValue(AUTHOR.LAST_NAME));

        final Record thirdRecord = result.get(2);
        assertEquals(3, thirdRecord.getValue(AUTHOR.ID).intValue());
        assertEquals("Stephen", thirdRecord.getValue(AUTHOR.FIRST_NAME));
        assertEquals("King", thirdRecord.getValue(AUTHOR.LAST_NAME));
    }
}
