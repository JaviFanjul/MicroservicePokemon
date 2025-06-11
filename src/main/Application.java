package main;

import io.vertx.core.Vertx;
import main.handler.PokemonRestVerticle;

public class Application {


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new PokemonRestVerticle());
    }
}