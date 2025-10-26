package org.example.benchmark;

import org.example.algorithms.KruskalMST;
import org.example.algorithms.PrimMST;
import org.example.graph.ListGraph;
import org.example.io.JSONLoader;
import org.example.io.dto.InputDataset;
import org.example.io.dto.GraphDTO;
import org.example.metrics.CSVWriter;
import org.example.metrics.Metrics;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 10)
@Measurement(iterations = 5, time = 15)
@Fork(1)
@State(Scope.Benchmark)

public class MSTBenchmark {

    @Param({"data/small_input.json",
            "data/medium_input.json",
            "data/large_input.json",
            "data/extra_large_input.json"})
    private String inputPath;

    private List<GraphDTO> graphs;
    private CSVWriter csvWriter;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        InputDataset dataset = JSONLoader.load(inputPath);
        graphs = dataset.getGraphs();
        csvWriter = new CSVWriter("results/mst_benchmark.csv");
    }

    @Benchmark
    public void runBenchmark() throws IOException {
        for (GraphDTO dto : graphs) {
            ListGraph g = dto.toListGraph();
            int vertexCount = g.getVerticesCount();
            int edgeCount = g.getEdgesCount();

            Metrics primM = new Metrics();
            long startPrim = System.nanoTime();
            PrimMST prim = new PrimMST(g, primM);
            prim.run();
            long endPrim = System.nanoTime();
            double timePrimMs = (endPrim - startPrim) / 1_000_000.0;
            primM.setExecutionTimeMs(timePrimMs);

            csvWriter.appendRow(
                    dto.getId(), vertexCount, edgeCount, "Prim",
                    prim.getTotalCost(),
                    primM.getComparisons(), primM.getFinds(),
                    primM.getUnions(), primM.getHeapOps(),
                    primM.getExecutionTimeMs()
            );

            Metrics krM = new Metrics();
            long startKr = System.nanoTime();
            KruskalMST kruskal = new KruskalMST(g, krM);
            kruskal.run();
            long endKr = System.nanoTime();
            double timeKrMs = (endKr - startKr) / 1_000_000.0;
            krM.setExecutionTimeMs(timeKrMs);

            csvWriter.appendRow(
                    dto.getId(), vertexCount, edgeCount, "Kruskal",
                    kruskal.getTotalCost(),
                    krM.getComparisons(), krM.getFinds(),
                    krM.getUnions(), krM.getHeapOps(),
                    krM.getExecutionTimeMs()
            );
        }
    }
}
