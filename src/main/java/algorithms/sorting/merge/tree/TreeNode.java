package algorithms.sorting.merge.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    String label;
    int depth;
    boolean isUnsorted;
    List<TreeNode> children = new ArrayList<>();

    public TreeNode(String label, int depth, boolean isUnsorted) {
        this.label = label;
        this.depth = depth;
        this.isUnsorted = isUnsorted;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}
