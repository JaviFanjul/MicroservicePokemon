package main.handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import main.api.PokemonApiClient;
import main.repository.PokemonRepository;
import main.service.PokemonService;

public class PokemonRestVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        // Create the Router to handle the routes
        Router router = Router.router(vertx);

        // Defining the REST endpoint
        // Using "path parameter" for the name, for example: /saludar/Mundo
        PokemonRepository repository = new PokemonRepository(vertx);
        PokemonService service = new PokemonService();
        PokemonApiClient client = new PokemonApiClient(vertx);

        router.get("/pokemon/:name").handler(new PokemonHandler(client,service, repository));

        // Creating the HTTP server and assigning the router
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("HTTP server initialized at port 8080...");
                    } else {
                        startPromise.fail(http.cause());
                        System.err.println("The server could not be initialized: " + http.cause().getMessage());
                    }
                });
    }
}