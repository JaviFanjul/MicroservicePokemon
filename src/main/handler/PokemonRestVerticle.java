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
        // Creamos un enrutador (Router) para manejar las rutas.
        Router router = Router.router(vertx);

        // Definimos el endpoint REST.
        // Usaremos un "path parameter" para el nombre, por ejemplo: /saludar/Mundo
        PokemonRepository repository = new PokemonRepository(vertx);
        PokemonService service = new PokemonService();
        PokemonApiClient client = new PokemonApiClient(vertx);

        router.get("/pokemon/:name").handler(new PokemonHandler(client,service, repository));

        // Creamos el servidor HTTP y le asignamos el enrutador.
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("Servidor HTTP iniciado en el puerto 8080");
                    } else {
                        startPromise.fail(http.cause());
                        System.err.println("No se pudo iniciar el servidor HTTP: " + http.cause().getMessage());
                    }
                });
    }
}