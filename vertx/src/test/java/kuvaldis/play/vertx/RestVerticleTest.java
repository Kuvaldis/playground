package kuvaldis.play.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(VertxUnitRunner.class)
public class RestVerticleTest {

    private Vertx vertx;

    @Before
    public void setUp(final TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.deployVerticle(RestVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(final TestContext context) throws Exception {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testHelloWorld(final TestContext context) throws Exception {
        final Async async = context.async();

        vertx.createHttpClient().getNow(9090, "localhost", "/products",
                response -> response.handler(body -> {
                    context.assertTrue(body.toString().contains("prod3568"));
                    context.assertTrue(body.toString().contains("prod7340"));
                    context.assertTrue(body.toString().contains("prod8643"));
                    async.complete();
                }));
    }
}
