package main.repository;

import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import main.model.Pokemon;
import io.vertx.core.Vertx;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.logging.Logger;

public class PokemonRepository implements PokemonRepositoryInterface {

    SqlClient client;
    Vertx vertx;
    private static final Logger logger = Logger.getLogger(PokemonRepository.class.getName());

    // Class constructor
    public PokemonRepository(Vertx vertx) {
        this.vertx = vertx;
        this.client = PostgresClientProvider.createClient(vertx);
    }

    // For making the query to the database and save the data
    @Override
    public Future<Integer> savePokemon(Pokemon p) {
        // SQL Query (adding 'RETURNING id' to get the entity key)
        String query = "INSERT INTO pokemon (name, npokedex, weakagainst, speed, attack, defense) VALUES ($1, $2, $3, $4, $5, $6) RETURNING id";
        // Creating a temporary tuple for saving the Pokemon data and associate it to the Query on the callback
        Tuple values = Tuple.of(p.getName(), p.getNumberPokedex(), p.getWeakAgainst(), p.getSpeed(), p.getAttack(), p.getDefense());

        // Use the SQLClient for saving the data into the database
        Promise<Integer> promise = Promise.promise();
        client
                .preparedQuery(query)
                .execute(values)
                .onSuccess(res -> {
                    RowSet<Row> rows = res;
                    if (rows.size() > 0) {
                        Row row = rows.iterator().next();
                        int id = row.getInteger("id"); // 'id' is now present in the Row
                        promise.complete(id);
                        logger.info("Pokemon data saved successfully with ID " + id +" for: " + p.getName()) ;
                    } else {
                        promise.fail("No ID returned from INSERT operation.");
                        logger.severe("No ID returned from INSERT operation.");
                    }
                })
                .onFailure(dbFailure -> {
                    promise.fail("Database error: " + dbFailure.getMessage());
                    logger.severe("Database error: " + dbFailure.getMessage());
                }); // API failure



        return promise.future();
    }




}
