package org.example.metrics;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Locale;

public class CSVWriter {
    private final Path csvPath;
    private boolean headerWritten = false;

    public CSVWriter(String filePath) {
        this.csvPath = Paths.get(filePath);
    }

    public void appendRow(int graphId, int vertices, int edges, String algorithm,
                          double totalCost, long comparisons, long finds,
                          long unions, long heapOps, double timeMs) throws IOException {

        if (!Files.exists(csvPath)) {
            Files.createDirectories(csvPath.getParent());
            Files.createFile(csvPath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                csvPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {

            if (!headerWritten && Files.size(csvPath) == 0) {
                writer.write("graph_id,vertices,edges,algorithm,total_cost,comparisons,finds,unions,heapOps,time_ms\n");
                headerWritten = true;
            }

            writer.write(String.format(Locale.US,
                    "%d,%d,%d,%s,%.0f,%d,%d,%d,%d,%.3f\n",
                    graphId, vertices, edges, algorithm, totalCost,
                    comparisons, finds, unions, heapOps, timeMs));
        }
    }
}
