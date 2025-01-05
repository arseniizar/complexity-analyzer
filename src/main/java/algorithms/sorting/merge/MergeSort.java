package algorithms.sorting.merge;

import algorithms.Algorithm;
import algorithms.sorting.merge.tree.TreeNode;
import algorithms.sorting.merge.tree.TreePanel;
import specifications.sorting.SortingSpecification;
import specifications.sorting.merge.MergeSortSpecification;
import utils.TotalCorrectnessChecker;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MergeSort extends Algorithm {
    private int treeComparisonCount = 0;

    public int[] sort(int[] S, int len, SortingSpecification spec) {
        dominantOperationCount = 0;

        if (!spec.validatePreconditions(S, len)) {
            throw new IllegalArgumentException("Preconditions failed for merge sort");
        }

        if (len <= 1) return S;

        int[][] halves = splitArray(S, len);
        int[] left = halves[0];
        int[] right = halves[1];

        int[] result = merge(sort(left, left.length, spec), left.length, sort(right, right.length, spec), right.length);

        if (!spec.validatePostconditions(null, result, result.length)) {
            throw new IllegalStateException("Postconditions failed for merge sort");
        }

        return result;
    }

    public int[] merge(int[] a1, int len1, int[] a2, int len2) {
        int i = 0, j = 0, k = 0;
        int[] result = new int[len1 + len2];

        // Dominating operation is the comparison in the while loop
        while (i < len1 && j < len2) {
            dominantOperationCount++;
            if (a1[i] < a2[j]) result[k++] = a1[i++];
            else result[k++] = a2[j++];
        }

        while (i < len1) result[k++] = a1[i++];
        while (j < len2) result[k++] = a2[j++];

        return result;
    }

    public void printMergeSortTree(int[] S, int len, int depth) {
        for (int i = 0; i < depth; i++) System.out.print("   ");
        System.out.println(arrayToString(S));

        if (len <= 1) return;

        int[][] halves = splitArray(S, len);
        int[] left = halves[0];
        int[] right = halves[1];

        printMergeSortTree(left, left.length, depth + 1);
        printMergeSortTree(right, right.length, depth + 1);
    }

    private int[][] splitArray(int[] array, int len) {
        int m = len / 2;
        int[] left = new int[m];
        int[] right = new int[len - m];

        System.arraycopy(array, 0, left, 0, m);
        System.arraycopy(array, m, right, 0, len - m);

        return new int[][]{left, right};
    }

    public String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void checkCorrectness() {
        // Define test cases
        List<int[]> testCases = Arrays.asList(
                new int[]{5, 3, 8, 6, 2},        // General unsorted array
                new int[]{10, 1, 3, 5, 7},       // Random array
                new int[]{100, 50, 25, 75, 10},  // Decreasing order
                new int[]{1, 2, 3, 4, 5},        // Already sorted array
                new int[]{},                     // Edge case: empty array
                new int[]{42}                    // Edge case: single element
        );


        // Algorithm to test
        Function<int[], int[]> algorithm = array -> {
            int[] copy = Arrays.copyOf(array, array.length); // Avoid modifying the original
            return new MergeSort().sort(copy, copy.length, new MergeSortSpecification());  // Call MergeSort
        };

        // Expected result using Java's built-in sorting
        Function<int[], int[]> expected = array -> {
            int[] copy = Arrays.copyOf(array, array.length);
            Arrays.sort(copy);
            return copy;
        };

        // Validate correctness
        boolean isCorrect = TotalCorrectnessChecker.checkCorrectness(algorithm, expected, testCases);

        // Output the result
        System.out.println("Merge Sort Total Correctness: " + (isCorrect ? "PASSED" : "FAILED"));
    }

    public void displayMergeSortTree(int[] S, int len) {
        JFrame frame = new JFrame("Merge Sort Recursive Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);

        // Build the tree data
        TreeNode root = buildTree(S, len, 0);

        TreePanel treePanel = new TreePanel(root, treeComparisonCount);
        JScrollPane scrollPane = new JScrollPane(treePanel);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private TreeNode buildTree(int[] S, int len, int depth) {
        // Represent the initial (unsorted) state
        TreeNode node = new TreeNode(arrayToString(S), depth, true);

        if (len <= 1) {
            // For single-element arrays, no further recursion is needed
            return new TreeNode(arrayToString(S), depth, false); // Sorted node
        }

        int[][] halves = splitArray(S, len);
        int[] left = halves[0];
        int[] right = halves[1];

        // Add child nodes for unsorted states
        TreeNode leftChild = buildTree(left, left.length, depth + 1);
        TreeNode rightChild = buildTree(right, right.length, depth + 1);

        node.addChild(leftChild);
        node.addChild(rightChild);

        // Represent the merged (sorted) state
        int[] merged = merge(left, left.length, right, right.length);
        treeComparisonCount += left.length + right.length - 1; // Add comparisons made during merge
        TreeNode sortedNode = new TreeNode(arrayToString(merged), depth, false);

        // Add the sorted node as a separate child to show the transition
        node.addChild(sortedNode);

        return node;
    }


    @Override
    public void visualize() {
//        int[] S = new int[]{5, 3, 8, 6, 2, 7, 4, 1};
//        displayMergeSortTree(S, S.length);
    }
}
