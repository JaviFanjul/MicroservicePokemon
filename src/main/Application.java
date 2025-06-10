package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import main.api.PokemonApiClient;
import main.model.Pokemon;
import main.service.PokemonService;
import main.repository.PokemonRepository;
import io.vertx.core.Future;

public class Application extends AbstractVerticle {


    public static void main(String[] args) {
        PokemonApiClient client = new PokemonApiClient();
        PokemonService service = new PokemonService();
        PokemonRepository repository = new PokemonRepository();

        // Llamamos al método y obtenemos el resultado asíncrono
        client.getPokemonData("magikarp").onComplete(ar -> {
            if (ar.succeeded()) {
                Pokemon p = new Pokemon(ar.result());
                p.setWeak(service.getWeakTypes(p));
                Future<Integer> i = repository.savePokemon(p).onComplete(saveResult -> {
                    if (saveResult.succeeded()) {
                        p.setId(saveResult.result());
                        System.out.println("Pokémon saved successfully!");
                        System.out.println(p.getData().toString(4));
                    } else {
                        System.out.println("Error saving Pokémon: " + saveResult.cause().getMessage());
                    }
                });


            } else {
                // Si hubo un error, lo mostramos
                System.out.println("Error fetching Pokémon data: " + ar.cause().getMessage());
            }
        });
    }
}