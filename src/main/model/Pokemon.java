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

    private String[] abilities;

    private String[] weakAgainst;

    public Pokemon() { } // Pokemon default constructor

    // Getters
    public String getName() {return this.name;}
    public int getNumberPokedex() {return this.numberPokedex;}
    public int getSpeed() {return this.speed;}
    public int[] getAttack() {return this.attack;}
    public int[] getDefense() {return this.defense;}
    public String[] getAbilities() {return this.abilities;}
    public String[] getWeakAgainst() {return this.weakAgainst;}
    public String[] getTypes(){
        return this.types;
    }

    // Setters
    public void setWeak(String[] weakTypes){
        this.weakAgainst = weakTypes;
    }
    public void setId(int id){this.id = id;}//setId

    // Pokemon constructor through JSON
    public Pokemon (JSONObject pokeData){
        // Get name
        this.name = pokeData.getString("name");

        // Get pokedex number
        this.numberPokedex = pokeData.getInt("pokedex_number");

        // Get stats
        JSONObject stats = pokeData.getJSONObject("stats");
        this.speed = stats.getInt("speed");
        this.attack = new int[2];
        this.attack[0] = stats.getInt("attack");
        this.attack[1] = stats.getInt("special-attack");
        this.defense = new int[2];
        this.defense[0] = stats.getInt("defense");
        this.defense[1] = stats.getInt("special-defense");

        // Get types
        JSONArray typesJson = pokeData.getJSONArray("types");
        String[] typesArray = new String[typesJson.length()];
        for (int i = 0; i < typesJson.length(); i++) {
            typesArray[i] = typesJson.getString(i);
        }
        this.types = typesArray;

        // Get habilities
        JSONArray abilitiesJson = pokeData.getJSONArray("abilities");
        String[] abilitiesArray = new String[abilitiesJson.length()];
        for (int i = 0; i < abilitiesJson.length(); i++) {
            abilitiesArray[i] = abilitiesJson.getString(i);
        }
        this.abilities = abilitiesArray;


        this.weakAgainst = null;
    } // Pokemon constructor through JSONObject


    public JSONObject getData(){
        JSONObject json = new JSONObject();

        // Simple atributes
        json.put("id", this.id);
        json.put("pokedex_number", this.numberPokedex);
        json.put("name", this.name);

        // Compound atributes: attack and defense
        JSONObject stats = new JSONObject();
        if (this.attack != null && this.attack.length >= 2) {
            stats.put("attack", this.attack[0]);
            stats.put("special-attack", this.attack[1]);
        }
        if (this.defense != null && this.defense.length >= 2) {
            stats.put("defense", this.defense[0]);
            stats.put("special-defense", this.defense[1]);
        }
        stats.put("speed", this.speed);

        json.put("stats", stats);

        // Types
        JSONArray typesArray = new JSONArray();
        if (this.types != null) {
            for (String t : this.types) {
                typesArray.put(t);
            }
        }
        json.put("types", typesArray);

        // Weaknesses
        JSONArray weakArray = new JSONArray();
        if (this.weakAgainst != null) {
            for (String w : this.weakAgainst) {
                weakArray.put(w);
            }
        }
        json.put("weak_against", weakArray);

        // Abilities
        JSONArray abilitiesArray = new JSONArray();
        if (this.abilities != null) {
            for (String a : this.abilities) {
                abilitiesArray.put(a);
            }
        }
        json.put("abilities", abilitiesArray);


        return json;
    } // getData

} // Pokemon class
