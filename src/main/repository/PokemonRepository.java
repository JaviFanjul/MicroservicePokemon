package main.repository;

import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import main.model.Pokemon;
import io.vertx.core.Vertx;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Tuple;

public class PokemonRepository implements PokemonRepositoryInterface {

    SqlClient client;
    public PokemonRepository() {
        this.client = PostgresClientProvider.createClient(Vertx.vertx());
    }

    @Override
    public Future<Void> savePokemon(Pokemon p) {
        String query = "INSERT INTO pokemon (name, npokedex, weakagainst, speed,attack, defense) VALUES ($1, $2, $3, $4, $5, $6)";
        Tuple values = Tuple.of(p.getName(), p.getNumberPokedex(), p.getWeakAgainst(), p.getSpeed(), p.getAttack(), p.getDefense());

        Promise<Void> promise = Promise.promise();

        client
                .preparedQuery(query)
                .execute(values)
                .onSuccess(res -> promise.complete())
                .onFailure(promise::fail);

        return promise.future();
    }




}
