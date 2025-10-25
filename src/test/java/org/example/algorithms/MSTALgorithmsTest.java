package org.example.algorithms;
import org.example.graph.ListGraph;
import org.example.metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MSTAlgorithmsTest {

    private ListGraph createSampleGraph() {
        ListGraph g = new ListGraph();
        g.addEdge("A", "B", 4);
        g.addEdge("A", "C", 3);
        g.addEdge("B", "C", 2);
        g.addEdge("B", "D", 5);
        g.addEdge("C", "D", 7);
        g.addEdge("C", "E", 8);
        g.addEdge("D", "E", 6);
        return g;
    }

    @Test
    void testPrimAndKruskalProduceSameMSTCost() {
        ListGraph g = createSampleGraph();

        Metrics mPrim = new Metrics();
        PrimMST prim = new PrimMST(g, mPrim);
        prim.run();

        Metrics mKruskal = new Metrics();
        KruskalMST kruskal = new KruskalMST(g, mKruskal);
        kruskal.run();

        double costPrim = prim.getTotalCost();
        double costKruskal = kruskal.getTotalCost();

        assertEquals(costPrim, costKruskal, 0.0001, "Prim and Kruskal MST costs must match");
        assertEquals(g.V() - 1, prim.getMstEdges().size(), "Prim MST should have V-1 edges");
        assertEquals(g.V() - 1, kruskal.getMstEdges().size(), "Kruskal MST should have V-1 edges");
        assertTrue(costPrim > 0, "MST cost must be positive");
    }

    @Test
    void testGraphIsConnectedAfterMST() {
        ListGraph g = createSampleGraph();
        PrimMST prim = new PrimMST(g, new Metrics());
        prim.run();

        long componentCount = prim.getComponentCount();
        assertEquals(1, componentCount, "Graph should become one connected component after MST");
    }

    @Test
    void testDisconnectedGraphHandledGracefully() {
        ListGraph g = new ListGraph();
        g.addEdge("A", "B", 3);
        g.addEdge("C", "D", 2);

        PrimMST prim = new PrimMST(g, new Metrics());
        prim.run();

        KruskalMST kruskal = new KruskalMST(g, new Metrics());
        kruskal.run();

        assertEquals(2, prim.getComponentCount(), "Prim should detect 2 disconnected components");
        assertEquals(2, kruskal.getComponentCount(), "Kruskal should detect 2 disconnected components");
        assertTrue(prim.getTotalCost() > 0, "Each component has local MST cost");
    }

    @Test
    void testMSTIsAcyclic() {
        ListGraph g = createSampleGraph();
        PrimMST prim = new PrimMST(g, new Metrics());
        prim.run();

        long expectedEdges = g.V() - prim.getComponentCount();
        assertEquals(expectedEdges, prim.getMstEdges().size(), "MST should be acyclic (no cycles)");
    }

    @Test
    void testPerformanceMetricsNonNegative() {
        ListGraph g = createSampleGraph();

        Metrics mPrim = new Metrics();
        PrimMST prim = new PrimMST(g, mPrim);
        prim.run();

        assertTrue(mPrim.getExecutionTimeMs() >= 0, "Execution time must be non-negative");
        assertTrue(mPrim.getComparisons() >= 0, "Comparisons count must be non-negative");
        assertTrue(mPrim.getHeapOps() >= 0, "HeapOps count must be non-negative");
        assertTrue(mPrim.getOperationsCount() > 0, "There should be recorded operations");
    }

    @Test
    void testKruskalPerformanceMetrics() {
        ListGraph g = createSampleGraph();

        Metrics m = new Metrics();
        KruskalMST kruskal = new KruskalMST(g, m);
        kruskal.run();

        assertTrue(m.getExecutionTimeMs() >= 0, "Execution time must be non-negative");
        assertTrue(m.getComparisons() >= 0, "Comparisons count must be non-negative");
        assertTrue(m.getFinds() >= 0, "Find operations must be non-negative");
        assertTrue(m.getUnions() >= 0, "Union operations must be non-negative");
        assertTrue(m.getOperationsCount() > 0, "There should be recorded operations");
    }

    @Test
    void testReproducibility() {
        ListGraph g = createSampleGraph();

        PrimMST prim1 = new PrimMST(g, new Metrics());
        PrimMST prim2 = new PrimMST(g, new Metrics());
        prim1.run();
        prim2.run();

        assertEquals(prim1.getTotalCost(), prim2.getTotalCost(), 0.0001,
                "Repeated runs of Prim must yield same MST cost");

        KruskalMST k1 = new KruskalMST(g, new Metrics());
        KruskalMST k2 = new KruskalMST(g, new Metrics());
        k1.run();
        k2.run();

        assertEquals(k1.getTotalCost(), k2.getTotalCost(), 0.0001,
                "Repeated runs of Kruskal must yield same MST cost");
    }

    @Test
    void testEdgeCases() {
        PrimMST primEmpty = new PrimMST(new ListGraph(), new Metrics());
        assertDoesNotThrow(primEmpty::run);
        assertEquals(0, primEmpty.getMstEdges().size());
        assertEquals(0.0, primEmpty.getTotalCost());

        ListGraph single = new ListGraph();
        single.addVertex("X");
        PrimMST primSingle = new PrimMST(single, new Metrics());
        primSingle.run();
        assertEquals(0, primSingle.getMstEdges().size());
        assertEquals(0.0, primSingle.getTotalCost(), 0.001);
    }
}
