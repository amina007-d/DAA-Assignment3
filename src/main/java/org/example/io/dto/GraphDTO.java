package org.example.io.dto;

import org.example.graph.ListGraph;
import java.util.List;

public class GraphDTO {
    private int id;
    private List<String> vertices;
    private List<EdgeDTO> edges;

    public int getId() { return id; }
    public List<String> getVertices() { return vertices; }
    public List<EdgeDTO> getEdges() { return edges; }

    public void setId(int id) { this.id = id; }
    public void setVertices(List<String> vertices) { this.vertices = vertices; }
    public void setEdges(List<EdgeDTO> edges) { this.edges = edges; }

    public ListGraph toListGraph() {
        ListGraph g = new ListGraph();
        if (vertices != null) {
            for (String v : vertices) {
                g.addVertex(v);
            }
        }
        if (edges != null) {
            for (EdgeDTO e : edges) {
                g.addEdge(e.getFrom(), e.getTo(), e.getWeight());
            }
        }
        return g;
    }
}
