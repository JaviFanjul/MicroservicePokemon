package main.handler;

import io.vertx.ext.web.RoutingContext;
import io.vertx.core.Handler;

public interface PokemonHandlerInterface extends Handler<RoutingContext> {

    void handle(RoutingContext context);
}
