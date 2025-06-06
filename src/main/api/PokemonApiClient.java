package main.api;
import main.model.Pokemon;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.json.JSONArray;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.core.Future;
import io.vertx.core.Promise;


public class PokemonApiClient implements PokemonApiClientInterface {

    //This class implements the PokemonApiClientInterface to interact with a Pokémon API.
    private final Vertx vertx;
    private final WebClient client;

    public PokemonApiClient() {
        this.vertx = Vertx.vertx(); // Inicia el vertx
        this.client = WebClient.create(vertx); // Crea el cliente web
    } //Constructor

    @Override
    public Future<JSONObject> getPokemonData(String name) {
        Promise<JSONObject> promise = Promise.promise();  // Creamos un promise para el resultado

        client.get(80, "pokeapi.co", "/api/v2/pokemon/" + name)
                .send(ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<?> response = ar.result();
                        String body = response.bodyAsString();
                        JSONObject json = new JSONObject(body);
                        //System.out.println(parseData(json).toString());
                        promise.complete(parseData(json));  // Si la petición es exitosa, completamos el promise
                    } else {
                        promise.fail(ar.cause());  // Si la petición falla, fallamos el promise
                    }
                });

        return promise.future();  // Retornamos el futuro asociado al promise
    }

    @Override
    public JSONObject parseData(JSONObject data) {
        JSONObject modifiedJson = new JSONObject();

        modifiedJson.put("name", data.getString("name"));
        modifiedJson.put("pokedex_number", data.getInt("id"));

        JSONObject statsJson = new JSONObject();
        JSONArray stats = data.getJSONArray("stats");
        for (int i = 0; i < stats.length(); i++) {
            JSONObject stat = stats.getJSONObject(i);
            String statName = stat.getJSONObject("stat").getString("name");
            int baseStat = stat.getInt("base_stat");
            statsJson.put(statName, baseStat);
        }

        modifiedJson.put("stats", statsJson);

        JSONArray typesJson = new JSONArray();
        JSONArray types = data.getJSONArray("types");
        for (int i = 0; i < types.length(); i++) {
            String typeName = types.getJSONObject(i).getJSONObject("type").getString("name");
            typesJson.put(typeName);
        }

        modifiedJson.put("types", typesJson);

        return modifiedJson;
    }

}
