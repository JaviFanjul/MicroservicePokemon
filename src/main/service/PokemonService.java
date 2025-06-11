package main.service;

import main.model.Pokemon;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;



public class PokemonService implements PokemonServiceInterface {

    private JSONObject typeChart;

    // Class constructor
    public PokemonService() {
        this.typeChart = loadTypeChart();
    }

    // For loading the JSON file containing all Pokemon types (private, only used here)
    private JSONObject loadTypeChart() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("resources/tableType.json");
            if (is == null) {
                throw new RuntimeException("Resource not found: resource/json/tableType.json");
            }
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new JSONObject(json);
        } catch (IOException e) {
            throw new RuntimeException("Error loading type_chart.json", e);
        }
    }

    // Method to calculate the weak types for the pokemon
    @Override
    public String[] getWeakTypes(Pokemon p) {
        // Map that saves the pokemon type and
        Map<String, Double> multipliers = new HashMap<>();

        for (String tipo : p.getTypes()) {
            JSONObject relaciones = typeChart.getJSONObject(tipo.toLowerCase());

            // Double damage
            for (Object o : relaciones.getJSONArray("double_damage_from")) {
                String t = (String) o;
                multipliers.merge(t, 2.0, (a, b) -> a * b);
            }
            // Half damage
            for (Object o : relaciones.getJSONArray("half_damage_from")) {
                String t = (String) o;
                multipliers.merge(t, 0.5, (a, b) -> a * b);
            }
            // No damage
            for (Object o : relaciones.getJSONArray("no_damage_from")) {
                String t = (String) o;
                multipliers.put(t, 0.0); // Absolute immunity
            }
        }

        return multipliers.entrySet().stream()
                .filter(e -> e.getValue() > 1.0) // Is weak if receives more damage than usual
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }
}
