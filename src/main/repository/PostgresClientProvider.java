package main.repository;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.SqlClient;

public class PostgresClientProvider {

    public static SqlClient createClient(Vertx vertx) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("pokemon_db")
                .setUser("postgres")
                .setPassword("postgres");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        return PgPool.pool(vertx, connectOptions, poolOptions);
    }
}
