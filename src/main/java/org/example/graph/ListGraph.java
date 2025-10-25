package org.example.graph;

import java.util.*;

public class ListGraph implements Graph {
    private final Map<String, List<Edge>> adjList = new HashMap<>();
    private int edgeCount = 0;

    @Override
    public int V() {
        return adjList.size();
    }

    @Override
    public int E() {
        return edgeCount;
    }

    @Override
    public boolean hasVertex(String v) {
        return adjList.containsKey(v);
    }

    @Override
    public void addVertex(String v) {
        adjList.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    public void addEdge(String u, String v, int w) {
        addVertex(u);
        addVertex(v);

        Edge e = new Edge(u, v, w);
        adjList.get(u).add(e);
        adjList.get(v).add(e);
        edgeCount++;
    }

    @Override
    public Collection<String> vertices() {
        return adjList.keySet();
    }

    @Override
    public Iterable<Edge> adj(String v) {
        return adjList.getOrDefault(v, Collections.emptyList());
    }

    @Override
    public Iterable<Edge> edges() {
        Set<Edge> all = new HashSet<>();
        for (List<Edge> list : adjList.values()) {
            all.addAll(list);
        }
        return all;
    }

    public int getVerticesCount() { return V(); }
    public int getEdgesCount() { return E(); }
}
