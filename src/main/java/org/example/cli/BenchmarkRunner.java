package org.example.cli;

import org.example.io.JSONLoader;
import org.example.io.JSONWriter;
import org.example.io.dto.GraphDTO;
import org.example.io.dto.InputDataset;
import org.example.graph.ListGraph;
import org.example.algorithms.PrimMST;
import org.example.algorithms.KruskalMST;
import org.example.metrics.Metrics;
import org.example.metrics.CSVWriter;

import java.io.File;
import java.util.*;

public class BenchmarkRunner {

    public static void main(String[] args) throws Exception {
        String[] inputFiles = {
                "data/small_input.json",
                "data/medium_input.json",
                "data/large_input.json",
                "data/extra_large_input.json",
        };

        CSVWriter csv = new CSVWriter("output/results.csv");

        for (String inputPath : inputFiles) {

            InputDataset dataset = JSONLoader.load(inputPath);
            List<Map<String, Object>> graphResults = new ArrayList<>();

            int graphIndex = 1;
            for (GraphDTO dto : dataset.getGraphs()) {
                ListGraph g = dto.toListGraph();

                Metrics mPrim = new Metrics();
                Metrics mKruskal = new Metrics();

                PrimMST prim = new PrimMST(g, mPrim);
                KruskalMST kruskal = new KruskalMST(g, mKruskal);

                prim.run();
                kruskal.run();

                double costPrim = prim.getTotalCost();
                double costKruskal = kruskal.getTotalCost();

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("graph_id", graphIndex);
                result.put("input_stats", Map.of(
                        "vertices", g.getVerticesCount(),
                        "edges", g.getEdgesCount()
                ));
                result.put("prim", Map.of(
                        "mst_edges", prim.getMstEdges(),
                        "total_cost", costPrim,
                        "operations_count", mPrim.getOperationsCount(),
                        "execution_time_ms", mPrim.getExecutionTimeMs()
                ));
                result.put("kruskal", Map.of(
                        "mst_edges", kruskal.getMstEdges(),
                        "total_cost", costKruskal,
                        "operations_count", mKruskal.getOperationsCount(),
                        "execution_time_ms", mKruskal.getExecutionTimeMs()
                ));
                graphResults.add(result);

                csv.appendRow(
                        graphIndex, g.getVerticesCount(), g.getEdgesCount(),
                        "Prim", costPrim,
                        mPrim.getComparisons(), mPrim.getFinds(),
                        mPrim.getUnions(), mPrim.getHeapOps(),
                        mPrim.getExecutionTimeMs()
                );
                csv.appendRow(
                        graphIndex, g.getVerticesCount(), g.getEdgesCount(),
                        "Kruskal", costKruskal,
                        mKruskal.getComparisons(), mKruskal.getFinds(),
                        mKruskal.getUnions(), mKruskal.getHeapOps(),
                        mKruskal.getExecutionTimeMs()
                );

                graphIndex++;
            }

            String outJsonPath = "output/" + new File(inputPath)
                    .getName().replace("input", "output");
            JSONWriter.write(outJsonPath, Map.of("results", graphResults));
        }
    }
}
