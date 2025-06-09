package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import main.api.PokemonApiClient;
import main.model.Pokemon;

public class Application extends AbstractVerticle {


    public static void main(String[] args) {
        PokemonApiClient client = new PokemonApiClient();
        // Llamamos al método y obtenemos el resultado asíncrono
        client.getPokemonData("pikachu").onComplete(ar -> {
            if (ar.succeeded()) {
                Pokemon p = new Pokemon(ar.result());
                System.out.println(p.getData().toString(4));
            } else {
                // Si hubo un error, lo mostramos
                System.out.println("Error fetching Pokémon data: " + ar.cause().getMessage());
            }
        });
    }
}