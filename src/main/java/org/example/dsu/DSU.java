package org.example.dsu;

import org.example.metrics.Metrics;
import java.util.HashMap;
import java.util.Map;

public class DSU {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();
    private final Metrics metrics;

    public DSU(Iterable<String> vertices) {
        this(vertices, null);
    }

    public DSU(Iterable<String> vertices, Metrics metrics) {
        this.metrics = metrics;
        for (String v : vertices) {
            parent.put(v, v);
            rank.put(v, 0);
        }
    }

    private void incFind() { if (metrics != null) metrics.incFinds(); }
    private void incUnion() { if (metrics != null) metrics.incUnions(); }
    private void incComparison() { if (metrics != null) metrics.incComparisons(); }

    public String find(String x) {
        incFind();
        String p = parent.get(x);
        if (!p.equals(x)) parent.put(x, find(p));
        return parent.get(x);
    }

    public boolean union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);

        if (rootA.equals(rootB)) {
            incComparison();
            return false;
        }

        incUnion();

        int rankA = rank.get(rootA);
        int rankB = rank.get(rootB);

        if (rankA < rankB) parent.put(rootA, rootB);
        else if (rankA > rankB) parent.put(rootB, rootA);
        else {
            parent.put(rootB, rootA);
            rank.put(rootA, rankA + 1);
        }
        return true;
    }

    public boolean connected(String a, String b) {
        return find(a).equals(find(b));
    }

    public long countComponents() {
        return parent.keySet().stream()
                .map(this::find)
                .distinct()
                .count();
    }
}

