package kuvaldis.play.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

public class TemplateHandlerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final TemplateEngine engine = ThymeleafTemplateEngine.create();
        final TemplateHandler handler = TemplateHandler.create(engine);
        final Router router = Router.router(vertx);
        router.get("/thymeleaf/*").handler(routingContext -> {
            routingContext.data().put("foo", "moo");
            routingContext.data().put("bar", "jar");
            routingContext.next();
        });
        router.get("/thymeleaf/*").handler(handler);

        vertx.createHttpServer().requestHandler(router::accept).listen(9090, result -> {
            if (result.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(result.cause());
            }
        });
    }
}
