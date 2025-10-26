package org.example.visualization;

import org.example.algorithms.*;
import org.example.graph.*;
import org.example.metrics.Metrics;
import org.example.visualization.GraphVisualizer;

import java.util.*;

public class Demo {
    public static void main(String[] args) {
        ListGraph g = new ListGraph();
        g.addEdge("A", "B", 1);
        g.addEdge("A", "C", 1);
        g.addEdge("B", "C", 1);
        g.addEdge("B", "D", 2);
        g.addEdge("C", "D", 2);
        g.addEdge("C", "E", 3);
        g.addEdge("D", "E", 3);

        System.out.println("Graph: " + g.V() + " vertices, " + g.E() + " edges\n");

        Metrics primMetrics = new Metrics();
        PrimMST prim = new PrimMST(g, primMetrics);
        prim.run();

        prim.getMstEdges().forEach(System.out::println);
        System.out.printf("Total cost: %.2f\n", (double) prim.getTotalCost());
        System.out.printf("Time: %.3f ms\n", primMetrics.getExecutionTimeMs());
        System.out.printf("Comparisons: %d | HeapOps: %d\n\n",
                primMetrics.getComparisons(), primMetrics.getHeapOps());

        Metrics kruskalMetrics = new Metrics();
        KruskalMST kruskal = new KruskalMST(g, kruskalMetrics);
        kruskal.run();

        kruskal.getMstEdges().forEach(System.out::println);
        System.out.printf("Total cost: %.2f\n", kruskal.getTotalCost());
        System.out.printf("Time: %.3f ms\n", kruskalMetrics.getExecutionTimeMs());
        System.out.printf("Comparisons: %d | Unions: %d | Finds: %d\n\n",
                kruskalMetrics.getComparisons(), kruskalMetrics.getUnions(), kruskalMetrics.getFinds());

        if (Math.abs(prim.getTotalCost() - kruskal.getTotalCost()) < 1e-6)
            System.out.println("Both algorithms produced MSTs with the same total cost");
        else
            System.out.println(" Costs differ — check algorithms!");

        Set<Edge> primEdges = new HashSet<>(prim.getMstEdges());
        Set<Edge> kruskalEdges = new HashSet<>(kruskal.getMstEdges());

        GraphVisualizer.show(g, primEdges);
        GraphVisualizer.show(g, kruskalEdges);
    }
}

