package org.example.algorithms;

import org.example.dsu.DSU;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.*;

public class KruskalMST {
    private final Graph graph;
    private final Metrics metrics;
    private final List<Edge> mstEdges = new ArrayList<>();
    private double totalCost = 0.0;
    private long componentCount;

    public KruskalMST(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public void run() {
        long start = System.nanoTime();

        List<Edge> edges = new ArrayList<>();
        Iterable<Edge> graphEdges = graph.edges();
        Iterator<Edge> edgeIterator = graphEdges.iterator();

        while (edgeIterator.hasNext()) {
            Edge e = edgeIterator.next();
            edges.add(e);
        }

        edges.sort((e1, e2) -> {
            metrics.incComparisons();
            return Double.compare(e1.weight(), e2.weight());
        });

        DSU dsu = new DSU(graph.vertices(), metrics);

        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            String u = e.from();
            String v = e.to();

            if (dsu.union(u, v)) {
                mstEdges.add(e);
                totalCost += e.weight();

                if (mstEdges.size() == graph.V() - 1) {
                    break;
                }
            }
        }

        componentCount = dsu.countComponents();

        long end = System.nanoTime();
        metrics.setExecutionTimeMs((end - start) / 1_000_000.0);
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public long getComponentCount() {
        return componentCount;
    }
}
