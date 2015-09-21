package kuvaldis.play.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(VertxUnitRunner.class)
public class MultipleRouteVerticleTest {

    private Vertx vertx;

    @Before
    public void setUp(final TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.deployVerticle(MultipleRouteVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(final TestContext context) throws Exception {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testHelloWorld(final TestContext context) throws Exception {
        final Async async = context.async();

        vertx.createHttpClient().getNow(9090, "localhost", "/multiple/routes/",
                response -> {
                    final AtomicInteger counter = new AtomicInteger(0);
                    response.handler(body -> {
                        context.assertTrue(body.toString().contains("route" + counter.incrementAndGet()));
                        if (counter.get() == 3) {
                            async.complete();
                        }
                    });
                });
    }
}
