package main.api;

import org.json.JSONObject;
import io.vertx.core.Future;

public interface PokemonApiClientInterface {

    //Return JSON with data for the specified pokemon name
    Future<JSONObject> getPokemonData(String name);
    // Parse data from pokemon JSON
    JSONObject parseData(JSONObject data);



}
