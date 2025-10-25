package org.example.algorithms;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.*;

public class PrimMST {
    private final Graph graph;
    private final Metrics metrics;
    private final Set<String> visited = new HashSet<>();
    private final List<Edge> mstEdges = new ArrayList<>();
    private int totalCost = 0;

    public PrimMST(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public void run() {
        long start = System.nanoTime();

        List<String> vertices = new ArrayList<>(graph.vertices());
        for (int i = 0; i < vertices.size(); i++) {
            String startVertex = vertices.get(i);
            if (!visited.contains(startVertex)) {
                runFrom(startVertex);
            }
        }

        long end = System.nanoTime();
        metrics.setExecutionTimeMs((end - start) / 1_000_000.0);
    }

    private void runFrom(String startVertex) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        visit(startVertex, pq);

        while (!pq.isEmpty()) {
            metrics.incHeapOps();
            metrics.incComparisons((long) (Math.log(graph.V()) / Math.log(2)));

            Edge e = pq.poll();
            String u = e.from();
            String v = e.to();

            metrics.incComparisons();
            if (visited.contains(u) && visited.contains(v)) {
                continue;
            }

            mstEdges.add(e);
            totalCost += e.weight();

            String next = visited.contains(u) ? v : u;
            visit(next, pq);
        }
    }

    private void visit(String vertex, PriorityQueue<Edge> pq) {
        visited.add(vertex);

        Iterable<Edge> adjEdges = graph.adj(vertex);
        Iterator<Edge> iterator = adjEdges.iterator();

        while (iterator.hasNext()) {
            Edge e = iterator.next();
            metrics.incComparisons();

            String other = e.other(vertex);

            if (!visited.contains(other)) {
                pq.add(e);
                metrics.incHeapOps();
                metrics.incComparisons((long) (Math.log(graph.V()) / Math.log(2)));
            } else {
                metrics.incComparisons();
            }
        }
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public long getComponentCount() {
        return graph.V() - mstEdges.size();
    }
}
