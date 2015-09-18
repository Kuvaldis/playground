package kuvaldis.play.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class HelloWorldVerticleTest {

    private Vertx vertx;

    @Before
    public void setUp(final TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.deployVerticle(HelloWorldVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(final TestContext context) throws Exception {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testHelloWorld(final TestContext context) throws Exception {
        final Async async = context.async();

        vertx.createHttpClient().getNow(9090, "localhost", "/",
                response -> response.handler(body -> {
                    context.assertTrue(body.toString().contains("Hello"));
                    async.complete();
                }));
    }
}
