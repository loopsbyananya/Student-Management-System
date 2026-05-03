package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom modern line chart component.
 * Draws a smooth line with gradient fill and data point highlights.
 */
public class LineChartPanel extends JPanel {

    private List<Integer> data = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private String title;

    public LineChartPanel(String title, List<Integer> data, List<String> labels) {
        this.title = title;
        this.data = data;
        this.labels = labels;
        setOpaque(false);
        setPreferredSize(new Dimension(0, 220));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.size() < 2) {
            drawNoData(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padding = 40;
        int labelHeight = 20;
        int chartW = w - padding * 2;
        int chartH = h - padding - labelHeight;

        // Draw background grid lines (horizontal)
        g2.setColor(Theme.BORDER_LIGHT);
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i <= 4; i++) {
            int y = padding + (chartH * i / 4);
            g2.draw(new Line2D.Double(padding, y, w - padding, y));
            
            // Y-axis labels (0, 25, 50, 75, 100)
            g2.setFont(Theme.FONT_SMALL);
            g2.setColor(Theme.TEXT_MUTED);
            g2.drawString(String.valueOf(100 - (i * 25)), padding - 30, y + 5);
        }

        // Calculate coordinates
        double xStep = (double) chartW / (data.size() - 1);
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            double x = padding + (i * xStep);
            double y = padding + chartH - (chartH * data.get(i) / 100.0);
            points.add(new Point2D.Double(x, y));
        }

        // Draw area fill (gradient)
        Path2D area = new Path2D.Double();
        area.moveTo(points.get(0).getX(), padding + chartH);
        for (Point2D p : points) {
            area.lineTo(p.getX(), p.getY());
        }
        area.lineTo(points.get(points.size() - 1).getX(), padding + chartH);
        area.closePath();

        GradientPaint areaGrad = new GradientPaint(
                0, padding, new Color(79, 70, 229, 60),
                0, padding + chartH, new Color(79, 70, 229, 0));
        g2.setPaint(areaGrad);
        g2.fill(area);

        // Draw line
        g2.setColor(Theme.PRIMARY);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < points.size() - 1; i++) {
            g2.draw(new Line2D.Double(points.get(i), points.get(i+1)));
        }

        // Draw points and labels
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            
            // Outer glow
            g2.setColor(new Color(79, 70, 229, 50));
            g2.fill(new Ellipse2D.Double(p.getX() - 6, p.getY() - 6, 12, 12));
            
            // Point
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(p.getX() - 4, p.getY() - 4, 8, 8));
            g2.setColor(Theme.PRIMARY);
            g2.draw(new Ellipse2D.Double(p.getX() - 4, p.getY() - 4, 8, 8));

            // X-axis Label
            if (labels != null && i < labels.size()) {
                g2.setFont(Theme.FONT_SMALL);
                g2.setColor(Theme.TEXT_SECONDARY);
                String label = labels.get(i);
                FontMetrics fm = g2.getFontMetrics();
                int lx = (int) (p.getX() - fm.stringWidth(label) / 2);
                g2.drawString(label, lx, padding + chartH + 20);
            }
        }

        g2.dispose();
    }

    private void drawNoData(Graphics g) {
        g.setFont(Theme.FONT_BODY);
        g.setColor(Theme.TEXT_MUTED);
        String msg = "Not enough data to display progress chart.";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }
}
