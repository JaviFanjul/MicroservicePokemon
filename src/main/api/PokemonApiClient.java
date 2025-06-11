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


    private final Vertx vertx;
    private final WebClient client;

    public PokemonApiClient(Vertx vertx) {
        this.vertx = vertx;
        this.client = WebClient.create(vertx);
    }

    @Override
    public Future<JSONObject> getPokemonData(String name) {
        Promise<JSONObject> promise = Promise.promise();

        client.get(80, "pokeapi.co", "/api/v2/pokemon/" + name)
                .send(ar -> {
                    if (ar.succeeded() && ar.result().statusCode() == 200) {
                        HttpResponse<?> response = ar.result();
                        String body = response.bodyAsString();
                        JSONObject json = new JSONObject(body);
                        promise.complete(parseData(json));
                    } else {
                        promise.fail("This pokemon does not exist or there was an error fetching the data.");
                    }
                });

        return promise.future();
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

        JSONArray abilitiesJson = new JSONArray();
        JSONArray abilities = data.getJSONArray("abilities");
        for (int i = 0; i < abilities.length(); i++) {
            String abilitieName = abilities.getJSONObject(i).getJSONObject("ability").getString("name");
            abilitiesJson.put(abilitieName);
        }

        modifiedJson.put("abilities", abilitiesJson);



        return modifiedJson;
    }

}
