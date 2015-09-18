package kuvaldis.play.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HelloWorldVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.createHttpServer()
                .requestHandler(r -> {
                    r.response().end("<h1>Hello there!</h1>");
                })
                .listen(9090, result -> {
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });
    }
}
