package algorithms.sorting.merge.tree;

import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {
    TreeNode root;
    int totalComparisons;

    public TreePanel(TreeNode root, int totalComparisons) {
        this.root = root;
        this.totalComparisons = totalComparisons;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(new Font("Arial", Font.BOLD, 12)); // Smaller font for compact visualization

        // Draw the number of comparisons at the top
        drawComparisons(g);

        // Dynamically calculate starting width for root node
        int startX = Math.max(getWidth() / 2, 400); // Adjust for compact layout
        drawNode(g, root, startX, 50, startX / 3);

        // Draw the legend at the bottom
        drawLegend(g);
    }

    private void drawComparisons(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 14)); // Smaller font for comparisons
        g.setColor(Color.BLACK);
        g.drawString("Total Comparisons: " + totalComparisons, 20, 30);
    }

    private void drawNode(Graphics g, TreeNode node, int x, int y, int xOffset) {
        // Set color based on "isUnsorted"
        if (node.isUnsorted) {
            g.setColor(Color.RED); // Unsorted is red
        } else {
            g.setColor(Color.BLACK); // Sorted is black
        }

        // Draw the node's label (array values only)
        g.drawString(node.label, x - 30, y);

        // Reset color for lines
        g.setColor(Color.BLACK);

        if (node.children.isEmpty()) {
            return;
        }

        // Adjust spacing dynamically using depth
        int childY = y + 40; // Reduced vertical spacing (previously 60 or 100)
        int childX = x - xOffset; // Adjust horizontal spacing dynamically

        for (TreeNode child : node.children) {
            g.drawLine(x, y + 5, childX, childY - 10); // Shorter connecting lines
            drawNode(g, child, childX, childY, Math.max(xOffset / 2, 30)); // Reduced minimum horizontal spacing
            childX += 2 * xOffset; // Reduced sibling node spacing
        }
    }

    private void drawLegend(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font for legend
        int x = 20;
        int y = getHeight() - 30;

        g.setColor(Color.RED);
        g.drawString("Red: Unsorted", x, y);

        g.setColor(Color.BLACK);
        g.drawString("Black: Sorted", x + 100, y);
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculate preferred size dynamically based on tree depth and width
        int width = Math.max(2000, getWidth()); // Smaller width
        int height = Math.max(800, (root.depth + 1) * 75); // Height based on depth, reduced by 50%
        return new Dimension(width, height);
    }
}
