package kuvaldis.play.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class MultipleRouteVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final Router router = Router.router(vertx);

        router.route("/multiple/routes/").handler(routingContext -> {
            final HttpServerResponse response = routingContext.response();
            response.setChunked(true); // required for the first route in router only
            response.write("route1");
            routingContext.next();
        });

        router.route("/multiple/routes/").handler(routingContext -> {
            final HttpServerResponse response = routingContext.response();
            response.write("route2");
            routingContext.next();
        });

        router.route("/multiple/routes/").handler(routingContext -> {
            final HttpServerResponse response = routingContext.response();
            response.write("route3");
            routingContext.response().end();
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(9090, result -> {
            if (result.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(result.cause());
            }
        });
    }
}
