package main.handler;

import io.vertx.ext.web.RoutingContext;

import main.api.PokemonApiClientInterface;
import main.model.Pokemon;
import main.repository.PokemonRepositoryInterface;
import main.service.PokemonServiceInterface;


public class PokemonHandler implements PokemonHandlerInterface {

    private final PokemonApiClientInterface pokemonApiClient; // Injects client for better testability and reusability
    private final PokemonServiceInterface pokemonService;
    private final PokemonRepositoryInterface pokemonRepository;

    // Constructor to inject dependencies
    public PokemonHandler(PokemonApiClientInterface pokemonApiClient, PokemonServiceInterface pokemonService, PokemonRepositoryInterface pokemonRepository) {
        this.pokemonApiClient = pokemonApiClient;
        this.pokemonService = pokemonService;
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void handle(RoutingContext context) {
        String pokemonName = context.request().getParam("name");

        // The request to the API is made here
        pokemonApiClient.getPokemonData(pokemonName)
                .onSuccess(pokemonData -> {

                    // For the requested pokemon, the weak types are calculated
                    Pokemon pokemon = new Pokemon(pokemonData);
                    pokemon.setWeak(pokemonService.getWeakTypes(pokemon));

                    // The method for saving data into database is invoked
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
                                        .end("There was an error on saving the Pokémon: " + saveFailure.getMessage());
                            }); // DB failure
                })
                .onFailure(apiFailure -> {
                    context.response()
                            .setStatusCode(500) // Internal Server Error
                            .putHeader("Content-Type", "text/plain")
                            .end(apiFailure.getMessage());
                }); // API failure

                // There are handlers for failures on both the DB request and the API request
    }
}