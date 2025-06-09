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

    public PokemonService() {
        this.typeChart = loadTypeChart();
    }

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

    @Override
    public String[] getWeakTypes(Pokemon p) {
        Map<String, Double> multipliers = new HashMap<>();

        for (String tipo : p.getTypes()) {
            JSONObject relaciones = typeChart.getJSONObject(tipo.toLowerCase());

            for (Object o : relaciones.getJSONArray("double_damage_from")) {
                String t = (String) o;
                multipliers.merge(t, 2.0, (a, b) -> a * b);
            }

            for (Object o : relaciones.getJSONArray("half_damage_from")) {
                String t = (String) o;
                multipliers.merge(t, 0.5, (a, b) -> a * b);
            }

            for (Object o : relaciones.getJSONArray("no_damage_from")) {
                String t = (String) o;
                multipliers.put(t, 0.0); // inmunidad absoluta
            }
        }

        return multipliers.entrySet().stream()
                .filter(e -> e.getValue() > 1.0) // débil si recibe más daño de lo normal
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }
}
