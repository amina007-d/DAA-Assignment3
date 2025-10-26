package org.example.graph;

public class Edge implements Comparable<Edge> {
    private final String from;
    private final String to;
    private final int weight;

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String from() { return from; }
    public String to() { return to; }
    public double weight() { return weight; }


    public String other(String vertex) {
        if (vertex.equals(from)) return to;
        else if (vertex.equals(to)) return from;
        else throw new IllegalArgumentException("Vertex not incident to edge");
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("%s-%s(%d)", from, to, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge)) return false;
        Edge e = (Edge) obj;
        return Double.compare(weight, e.weight) == 0 &&
                ((from.equals(e.from) && to.equals(e.to)) ||
                        (from.equals(e.to) && to.equals(e.from)));
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode() + Double.hashCode(weight);
    }
}
