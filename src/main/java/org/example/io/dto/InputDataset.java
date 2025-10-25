package org.example.io.dto;

import org.example.graph.ListGraph;
import java.util.ArrayList;
import java.util.List;

public class InputDataset {

    private List<GraphDTO> graphs;

    public List<GraphDTO> getGraphs() {
        return graphs;
    }

    public void setGraphs(List<GraphDTO> graphs) {
        this.graphs = graphs;
    }

    public List<ListGraph> toGraphList() {
        List<ListGraph> list = new ArrayList<>();
        if (graphs == null) return list;

        for (GraphDTO dto : graphs) {
            ListGraph g = new ListGraph();

            if (dto.getVertices() != null) {
                for (String v : dto.getVertices()) {
                    g.addVertex(v);
                }
            }

            if (dto.getEdges() != null) {
                for (EdgeDTO e : dto.getEdges()) {
                    g.addEdge(e.getFrom(), e.getTo(), e.getWeight());
                }
            }

            list.add(g);
        }

        return list;
    }
}
