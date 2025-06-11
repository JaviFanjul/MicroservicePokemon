package main;

import io.vertx.core.Vertx;
import main.handler.PokemonRestVerticle;

public class Application {

    public static void main(String[] args) {
        // Starting the vertx server here
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new PokemonRestVerticle());
    }
}