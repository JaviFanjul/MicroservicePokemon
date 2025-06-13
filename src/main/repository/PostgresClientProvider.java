package main.repository;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.SqlClient;
import io.github.cdimascio.dotenv.Dotenv;

public class PostgresClientProvider {

    private static final Dotenv dotenv = Dotenv.load();
    // Conection to the SQL Server (locally)
    public static SqlClient createClient(Vertx vertx) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(Integer.parseInt(dotenv.get("DB_PORT")))
                .setHost(dotenv.get("DB_HOST"))
                .setDatabase(dotenv.get("DB_NAME"))
                .setUser(dotenv.get("DB_USER"))
                .setPassword(dotenv.get("DB_PASSWORD"));

        // Pool of size 5 to prevent too many requests
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        return PgPool.pool(vertx, connectOptions, poolOptions);
    }
}
