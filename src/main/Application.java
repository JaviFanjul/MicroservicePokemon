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
                // Si todo fue bien, mostramos el JSON
                System.out.println("Pokémon data: " + ar.result().toString(4));  // Imprime el JSON de forma legible
            } else {
                // Si hubo un error, lo mostramos
                System.out.println("Error fetching Pokémon data: " + ar.cause().getMessage());
            }
        });
    }
}