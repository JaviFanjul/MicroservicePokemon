package main.handler;

import io.vertx.ext.web.RoutingContext;
import main.api.PokemonApiClient;
import main.model.Pokemon;
import main.service.PokemonService;
import main.repository.PokemonRepository;



public class PokemonHandler implements PokemonHandlerInterface {

    private final PokemonApiClient pokemonApiClient; // Inyecta el cliente para mejor testabilidad y reutilización
    private final PokemonService pokemonService;
    private final PokemonRepository pokemonRepository;

    // Constructor para inyectar las dependencias
    public PokemonHandler(PokemonApiClient pokemonApiClient, PokemonService pokemonService, PokemonRepository pokemonRepository) {
        this.pokemonApiClient = pokemonApiClient;
        this.pokemonService = pokemonService;
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void handle(RoutingContext context) {
        String pokemonName = context.request().getParam("name");

        pokemonApiClient.getPokemonData(pokemonName)
                .onSuccess(pokemonData -> {

                    Pokemon pokemon = new Pokemon(pokemonData);
                    pokemon.setWeak(pokemonService.getWeakTypes(pokemon));


                    pokemonRepository.savePokemon(pokemon)
                            .onSuccess(id -> {
                                pokemon.setId(id);
                                System.out.println("Pokémon saved successfully! ID: " + id);
                                System.out.println(pokemon.getData().toString(4));
                                context.response()
                                        .setStatusCode(201)
                                        .putHeader("Content-Type", "application/json")
                                        .end(pokemon.getData().toString(4));
                            })
                            .onFailure(saveFailure -> {
                                System.err.println("Error saving Pokémon: " + saveFailure.getMessage());
                                context.response()
                                        .setStatusCode(500) // Internal Server Error
                                        .putHeader("Content-Type", "text/plain")
                                        .end("Error al guardar el Pokémon: " + saveFailure.getMessage());
                            });
                })
                .onFailure(apiFailure -> {
                    context.response()
                            .setStatusCode(500) // Internal Server Error
                            .putHeader("Content-Type", "text/plain")
                            .end("Error al obtener datos del Pokemon");
                });
    }
}