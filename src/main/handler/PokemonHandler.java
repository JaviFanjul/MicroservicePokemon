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

        // 1. Obtener datos del Pokémon de forma asíncrona
        pokemonApiClient.getPokemonData(pokemonName)
                .onSuccess(pokemonData -> {
                    if (!pokemonData.has("name")) {
                        // Si no se encuentra el Pokémon, respondemos con un error 404
                        context.response()
                                .setStatusCode(404)
                                .putHeader("Content-Type", "text/plain")
                                .end("Pokémon no encontrado: " + pokemonName);
                        return;
                    }

                    // 2. Crear el objeto Pokémon y obtener debilidades
                    Pokemon pokemon = new Pokemon(pokemonData);
                    pokemon.setWeak(pokemonService.getWeakTypes(pokemon));

                    // 3. Guardar el Pokémon en el repositorio de forma asíncrona
                    pokemonRepository.savePokemon(pokemon)
                            .onSuccess(id -> {
                                pokemon.setId(id); // Asignar el ID generado por la base de datos
                                System.out.println("Pokémon saved successfully! ID: " + id);
                                System.out.println(pokemon.getData().toString(4)); // Usa encodePrettily para mejor formato

                                context.response()
                                        .setStatusCode(201) // Código de estado 201 para creación exitosa
                                        .putHeader("Content-Type", "application/json")
                                        .end(pokemon.getData().toString(4)); // Envía el JsonObject como respuesta
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
                    System.err.println("Error al obtener datos del Pokémon de la API: " + apiFailure.getMessage());
                    // Podrías diferenciar entre errores de red, timeouts, etc.
                    context.response()
                            .setStatusCode(500) // Internal Server Error
                            .putHeader("Content-Type", "text/plain")
                            .end("Error al obtener datos del Pokémon: " + apiFailure.getMessage());
                });
    }
}