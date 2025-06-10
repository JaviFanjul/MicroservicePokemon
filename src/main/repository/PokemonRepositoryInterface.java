package main.repository;

import main.model.Pokemon;
import io.vertx.core.Future;

public interface PokemonRepositoryInterface{

    Future<Integer> savePokemon(Pokemon p);
}
