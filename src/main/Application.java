package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class Application extends AbstractVerticle {

    @Override
    public void start() {
        // Crear un servidor HTTP en el puerto 8080
        vertx.createHttpServer()
                .requestHandler(req -> {
                    // Responder con "Hello, World!"
                    req.response().end("Hello, World!");
                })
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        System.out.println("Servidor iniciado en el puerto 8080");
                    } else {
                        System.err.println("Error al iniciar el servidor: " + result.cause());
                    }
                });
    }

    public static void main(String[] args) {
        // Crear un Vertx instance y desplegar el verticle
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Application());
    }
}