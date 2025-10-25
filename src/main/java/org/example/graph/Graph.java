package org.example.graph;

import java.util.Collection;

public interface Graph {
    int V();
    int E();

    boolean hasVertex(String v);
    void addVertex(String v);
    void addEdge(String u, String v, int w);

    Collection<String> vertices();
    Iterable<Edge> adj(String v);
    Iterable<Edge> edges();
}
