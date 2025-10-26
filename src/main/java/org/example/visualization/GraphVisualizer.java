package org.example.visualization;

import org.example.graph.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GraphVisualizer extends JPanel {
    private final Graph graph;
    private final Set<Edge> mstEdges;

    public GraphVisualizer(Graph graph, Set<Edge> mstEdges) {
        this.graph = graph;
        this.mstEdges = mstEdges;
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        int n = graph.V();
        int radius = 300;
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        java.util.List<String> verts = new ArrayList<>(graph.vertices());
        Map<String, Point> coords = new HashMap<>();
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            coords.put(verts.get(i), new Point(x, y));
        }

        for (Edge e : graph.edges()) {
            Point p1 = coords.get(e.from());
            Point p2 = coords.get(e.to());


            if (mstEdges.contains(e))
                g2.setColor(new Color(220, 0, 0));
            else
                g2.setColor(new Color(180, 180, 180));

            g2.drawLine(p1.x, p1.y, p2.x, p2.y);

            // Подпись веса в середине ребра
            int mx = (p1.x + p2.x) / 2;
            int my = (p1.y + p2.y) / 2;
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 13));
            g2.drawString(String.valueOf((int) e.weight()), mx - 5, my - 5);
        }

        for (String v : verts) {
            Point p = coords.get(v);

            g2.setColor(Color.BLACK);
            g2.fillOval(p.x - 10, p.y - 10, 20, 20);

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(v, p.x + 15, p.y + 5);
        }
    }

    public static void show(Graph g, Set<Edge> mst) {
        JFrame frame = new JFrame("Graph MST Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GraphVisualizer(g, mst));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

