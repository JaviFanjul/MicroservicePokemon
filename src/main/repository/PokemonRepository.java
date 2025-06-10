package main.repository;

import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import main.model.Pokemon;
import io.vertx.core.Vertx;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class PokemonRepository implements PokemonRepositoryInterface {

    SqlClient client;
    public PokemonRepository() {
        this.client = PostgresClientProvider.createClient(Vertx.vertx());
    }

    @Override
    public Future<Integer> savePokemon(Pokemon p) {
        // Añade RETURNING id a la consulta
        String query = "INSERT INTO pokemon (name, npokedex, weakagainst, speed, attack, defense) VALUES ($1, $2, $3, $4, $5, $6) RETURNING id";
        Tuple values = Tuple.of(p.getName(), p.getNumberPokedex(), p.getWeakAgainst(), p.getSpeed(), p.getAttack(), p.getDefense());

        Promise<Integer> promise = Promise.promise();

        client
                .preparedQuery(query)
                .execute(values)
                .onSuccess(res -> {
                    RowSet<Row> rows = res;
                    if (rows.size() > 0) {
                        Row row = rows.iterator().next();
                        int id = row.getInteger("id"); // Ahora 'id' estará presente en el Row
                        promise.complete(id);
                    } else {
                        promise.fail("No ID returned from INSERT operation.");
                    }
                })
                .onFailure(promise::fail);

        return promise.future();
    }




}
