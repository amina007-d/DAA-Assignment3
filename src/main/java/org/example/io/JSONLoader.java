package org.example.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.io.dto.InputDataset;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JSONLoader {

    public static InputDataset load(String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        InputStream stream = JSONLoader.class.getClassLoader().getResourceAsStream(path);
        if (stream != null) {
            try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                return gson.fromJson(reader, InputDataset.class);
            }
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Cannot find resource: " + path);
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, InputDataset.class);
        }
    }
}
