package org.example.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONWriter {

    public static void writeResults(List<Map<String, Object>> results, String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> wrapper = Map.of("results", results);
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(wrapper, writer);
        }
    }

    public static void write(String path, Map<String, Object> resultData) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(resultData, writer);
        }
    }
}
