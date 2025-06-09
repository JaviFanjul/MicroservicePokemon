package main.model;

import org.json.JSONObject;
import org.json.JSONArray;

public class Pokemon {

    // Internal atributes
    private int id;

    private int numberPokedex;

    private String name;

    private int speed;

    private int[] attack;

    private int[] defense;

    private String[] types;

    private String[] weakAgainst;

    public Pokemon() {
        // Default constructor
    }//Pokemon default constructor

    public Pokemon(int id, String name, int numberPokedex, String[] types, String[] weakAgainst, int speed, int[] attack, int[] defense) {
        this.id = id;
        this.name = name;
        this.numberPokedex = numberPokedex;
        this.types = types;
        this.weakAgainst = weakAgainst;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
    } //Pokemon constructor

    public Pokemon (JSONObject pokeData){
        //Get name
        this.name = pokeData.getString("name");

        //Get pokedex number
        this.numberPokedex = pokeData.getInt("pokedex_number");

        //Get stats
        JSONObject stats = pokeData.getJSONObject("stats");

        this.speed = stats.getInt("speed");

        this.attack = new int[2];
        this.attack[0] = stats.getInt("attack");
        this.attack[1] = stats.getInt("special-attack");

        this.defense = new int[2];
        this.defense[0] = stats.getInt("defense");
        this.defense[1] = stats.getInt("special-defense");

        //Get types
        JSONArray typesJson = pokeData.getJSONArray("types");
        String[] typesArray = new String[typesJson.length()];
        for (int i = 0; i < typesJson.length(); i++) {
            typesArray[i] = typesJson.getString(i);
        }

        this.types = typesArray;

        this.weakAgainst = null;
    }
    void setWeak(String[] weakTypes){

    }

    public JSONObject getData(){
        JSONObject json = new JSONObject();

        // Atributos simples
        json.put("id", this.id);
        json.put("pokedex_number", this.numberPokedex);
        json.put("name", this.name);
        json.put("speed", this.speed);

        // Atributos compuestos: ataque y defensa
        JSONObject stats = new JSONObject();
        if (this.attack != null && this.attack.length >= 2) {
            stats.put("attack", this.attack[0]);
            stats.put("special-attack", this.attack[1]);
        }
        if (this.defense != null && this.defense.length >= 2) {
            stats.put("defense", this.defense[0]);
            stats.put("special-defense", this.defense[1]);
        }
        json.put("stats", stats);

        // Tipos
        JSONArray typesArray = new JSONArray();
        if (this.types != null) {
            for (String t : this.types) {
                typesArray.put(t);
            }
        }
        json.put("types", typesArray);

        // Debilidades
        JSONArray weakArray = new JSONArray();
        if (this.weakAgainst != null) {
            for (String w : this.weakAgainst) {
                weakArray.put(w);
            }
        }
        json.put("weak_against", weakArray);

        return json;
    }

}//Pokemon class
