package kuvaldis.play.jooq;

import kuvaldis.play.jooq.generated.public_.tables.Book;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.junit.Test;

import java.sql.Connection;

import static junit.framework.Assert.assertEquals;
import static kuvaldis.play.jooq.DbUtils.connection;
import static kuvaldis.play.jooq.DbUtils.context;
import static kuvaldis.play.jooq.generated.public_.tables.Author.AUTHOR;
import static kuvaldis.play.jooq.generated.public_.tables.Book.BOOK;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.trim;

public class JooqTest {

    @Test
    public void testAuthorsList() throws Exception {
        final DSLContext context = context();
        final Result<Record> result = context.select().from(AUTHOR).limit(3).fetch();

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

    @Test
    public void testConcatAndTrim() throws Exception {
        final DSLContext context = context();
        final Result<Record1<String>> result = context
                .select(concat(trim(AUTHOR.FIRST_NAME), trim(AUTHOR.LAST_NAME)))
                .from(AUTHOR)
                .fetch();
        final Record1<String> thirdRecord = result.get(2);
        assertEquals("StephenKing", thirdRecord.value1());
    }

    @Test
    public void testWhere() throws Exception {
        final DSLContext context = context();
        final Result<Record> result = context.select()
                .from(BOOK)
                .where(BOOK.TITLE.like("Animal%"))
                .fetch();
        assertEquals(1, result.size());
        final Record record = result.get(0);
        assertEquals(2, record.getValue(BOOK.ID).intValue());
    }

    @Test(expected = DataAccessException.class)
    public void testConfiguration() throws Exception {
        final Connection connection = connection();
        final Configuration configuration = new DefaultConfiguration().set(connection).set(SQLDialect.H2);
        final String noInsertsIndicator = "kuvaldis.play.jooq.no-iserts";
        configuration.set(new DefaultExecuteListenerProvider(new DefaultExecuteListener() {
            @Override
            public void start(ExecuteContext ctx) {
                if (Boolean.TRUE.equals(ctx.configuration().data(noInsertsIndicator))) {

                    // If active, fail this execution, if an INSERT statement is being executed
                    if (ctx.query() instanceof Insert) {
                        throw new DataAccessException("No INSERT statements allowed");
                    }
                }
            }
        }));
        configuration.data(noInsertsIndicator, Boolean.TRUE);
        final DSLContext context = DSL.using(configuration);
        context.insertInto(AUTHOR)
                .columns(AUTHOR.ID, AUTHOR.LAST_NAME)
                .values(6, "Kuvaldis")
                .execute();
    }
}
