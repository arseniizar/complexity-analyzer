package algorithms.sorting;

import algorithms.Algorithm;
import algorithms.AlgorithmPreparer;
import specifications.SelectionSortSpecification;
import specifications.SortingSpecification;
import utils.TotalCorrectnessChecker;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SelectionSort extends Algorithm {
    /**
     * Sorts an array in ascending order
     *
     * @param S    - int[]
     * @param len  - int length of S
     * @param spec - SortingSpecification for validation
     */
    public void sort(final int[] S, int len, SortingSpecification spec) {
        dominantOperationCount = 0;
        // Validate preconditions
        if (!spec.validatePreconditions(S, len)) {
            throw new IllegalArgumentException("Preconditions failed for selection sort");
        }

        int i = 0;
        while (i < len) {
            int mini = indexOfMin(S, i, len);
            swap(S, i, mini);
            i++;
        }

        // Validate postconditions
        if (!spec.validatePostconditions(null, S, len)) {
            throw new IllegalStateException("Postconditions failed for selection sort");
        }
    }

    private int indexOfMin(final int[] S, int i, final int len) {
        int mini = i;
        for (int j = i + 1; j < len; j++) {
            if (S[j] < S[mini]) {
                mini = j;
            }
            dominantOperationCount++;
        }
        return mini;
    }

    private void swap(final int[] S, int i, int j) {
        int tmp = S[i];
        S[i] = S[j];
        S[j] = tmp;
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
            new SelectionSort().sort(copy, copy.length, new SelectionSortSpecification());
            return copy;
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
        System.out.println("Selection Sort Total Correctness: " + (isCorrect ? "PASSED" : "FAILED"));
    }

    public static class SelectionSortRunnable implements Runnable, AlgorithmPreparer {
        private int[] S;
        private SortingSpecification spec;
        private SelectionSort instance;

        public SelectionSortRunnable(SelectionSort instance) {
            this.instance = instance;
        }

        public SelectionSortRunnable(final int[] S, SortingSpecification spec, SelectionSort instance) {
            this.S = S;
            this.spec = spec;
            this.instance = instance;
        }

        @Override
        public Runnable prepareRunnable(int n) {
            long prepStartTime = System.nanoTime();

            int[] S = new int[n];
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                S[i] = random.nextInt(10_000);
            }

            long prepEndTime = System.nanoTime();
            System.out.println("Array preparation time for input size " + n + ": "
                    + ((prepEndTime - prepStartTime) / 1e6) + " ms");

            return new SelectionSortRunnable(S, new SelectionSortSpecification(), this.instance);
        }

        @Override
        public void run() {
            instance.sort(S, S.length, spec);
        }
    }

}
