package algorithms.searching;

import algorithms.Algorithm;
import org.jetbrains.annotations.NotNull;
import specifications.BinarySearchSpecification;
import algorithms.AlgorithmPreparer;
import utils.TotalCorrectnessChecker;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class BinarySearch extends Algorithm {

    public int search(int[] S, int len, int key, @NotNull BinarySearchSpecification spec) {
        dominantOperationCount = 0;
        if (!spec.validatePreconditions(S, len)) {
            throw new IllegalArgumentException("Preconditions not satisfied for binary search");
        }

        int l = 0;
        int r = len - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;
            dominantOperationCount++;

            if (S[m] == key) {
                if (!spec.validatePostconditions(m, S, key)) {
                    throw new IllegalStateException("Postconditions not satisfied for binary search");
                }
                return m;
            }
            dominantOperationCount++;
            if (S[m] > key) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }

        if (!spec.validatePostconditions(-1, S, key)) {
            throw new IllegalStateException("Postconditions not satisfied for binary search");
        }

        return -1;
    }

    @Override
    public void checkCorrectness() {
        BinarySearchSpecification spec = new BinarySearchSpecification();

        List<int[]> testCases = Arrays.asList(
                new int[]{1, 2, 3, 4, 5},
                new int[]{10, 20, 30, 40},
                new int[]{100, 200, 300, 400, 500},
                new int[]{} // Edge case: empty array
        );

        Function<int[], Integer> algorithm = array -> {
            if (array.length == 0) {
                return -1;
            }
            return search(array, array.length, array[array.length / 2], spec);
        };

        Function<int[], Integer> expected = array -> {
            if (array.length == 0) return -1;
            int key = array[array.length / 2];
            return Arrays.binarySearch(array, key);
        };

        boolean isCorrect = TotalCorrectnessChecker.checkCorrectness(algorithm, expected, testCases);

        System.out.println("Binary Search Total Correctness: " + (isCorrect ? "PASSED" : "FAILED"));
    }

    public static class BinarySearchRunnable implements Runnable, AlgorithmPreparer {
        private int[] array;
        private int key;
        private BinarySearchSpecification spec;
        private final BinarySearch instance;

        public BinarySearchRunnable(BinarySearch instance) {
            this.instance = instance;
        }

        public BinarySearchRunnable(int[] array, int key, BinarySearchSpecification spec, BinarySearch instance) {
            this.array = array;
            this.key = key;
            this.spec = spec;
            this.instance = instance;
        }

        @Override
        public void run() {
            instance.search(array, array.length, key, spec);
        }

        @Override
        public Runnable prepareRunnable(int n) {
            long prepStartTime = System.nanoTime();

            // Array preparation
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = i;
            }
            Arrays.sort(array);

            long prepEndTime = System.nanoTime();
            System.out.println("Array preparation time for input size " + n + ": "
                    + ((prepEndTime - prepStartTime) / 1e6) + " ms");

            return new BinarySearchRunnable(array, n / 2, new BinarySearchSpecification(), this.instance);
        }
    }

}
