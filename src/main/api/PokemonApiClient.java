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

    private final WebClient client;

    public PokemonApiClient(Vertx vertx) {
        this.client = WebClient.create(vertx);
    }

    // For requesting the data to the Pokemon API
    @Override
    public Future<JSONObject> getPokemonData(String name) {
        Promise<JSONObject> promise = Promise.promise();

        // Vertex client requests the specified pokemon to the API
        client.get(80, "pokeapi.co", "/api/v2/pokemon/" + name)
                .send(ar -> {
                    if (ar.result().statusCode() == 200) {

                        String data = ar.result().bodyAsString();

                        // A JSON is created with the body
                        JSONObject json = new JSONObject(data);

                        // parseData method is invoked to give proper format
                        promise.complete(parseData(json));

                    } else {
                        String errorMessage = ar.result().bodyAsString();
                        promise.fail(errorMessage);
                    }
                });

        return promise.future();
    }

    // For parsing the data from the JSON
    @Override
    public JSONObject parseData(JSONObject data) {

        // New JSON for placing the parsed data
        JSONObject modifiedJson = new JSONObject();

        // Name and Pokedex number
        modifiedJson.put("name", data.getString("name"));
        modifiedJson.put("pokedex_number", data.getInt("id"));

        // New JSON to save the pokemon stats
        JSONObject statsJson = new JSONObject();
        JSONArray stats = data.getJSONArray("stats");
        for (int i = 0; i < stats.length(); i++) {
            JSONObject stat = stats.getJSONObject(i);
            String statName = stat.getJSONObject("stat").getString("name");
            int baseStat = stat.getInt("base_stat");
            statsJson.put(statName, baseStat);
        }
        // Add the stats JSON to the parsed JSON
        modifiedJson.put("stats", statsJson);

        // New JSON to save the pokemon types
        JSONArray typesJson = new JSONArray();
        JSONArray types = data.getJSONArray("types");
        for (int i = 0; i < types.length(); i++) {
            String typeName = types.getJSONObject(i).getJSONObject("type").getString("name");
            typesJson.put(typeName);
        }
        // Add the types JSON to the parsed JSON
        modifiedJson.put("types", typesJson);

        // New JSON to save the pokemon abilities
        JSONArray abilitiesJson = new JSONArray();
        JSONArray abilities = data.getJSONArray("abilities");
        for (int i = 0; i < abilities.length(); i++) {
            String abilitieName = abilities.getJSONObject(i).getJSONObject("ability").getString("name");
            abilitiesJson.put(abilitieName);
        }
        // Add the types JSON to the parsed JSON
        modifiedJson.put("abilities", abilitiesJson);



        return modifiedJson;
    }

}
