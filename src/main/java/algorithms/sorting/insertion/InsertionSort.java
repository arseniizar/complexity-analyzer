package algorithms.sorting.insertion;

import algorithms.Algorithm;
import specifications.sorting.SortingSpecification;
import specifications.sorting.insertion.InsertionSortSpecification;
import utils.TotalCorrectnessChecker;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class InsertionSort extends Algorithm {
    /**
     * Sorts an array in ascending order using Insertion Sort.
     *
     * @param arr  The array to be sorted.
     * @param len  The length of the array.
     * @param spec The specification for correctness validation.
     */
    public void sort(final int[] arr, int len, SortingSpecification spec) {
        dominantOperationCount = 0;

        // Validate preconditions
        if (!spec.validatePreconditions(arr, len)) {
            throw new IllegalArgumentException("Preconditions failed for insertion sort");
        }

        for (int next = 1; next < len; next++) {
            int curr = next;
            int temp = arr[next];
            while ((curr > 0) && (temp < arr[curr - 1])) {
                arr[curr] = arr[curr - 1];
                curr--;
                dominantOperationCount++;
            }
            arr[curr] = temp;
            dominantOperationCount++;
        }

        // Validate postconditions
        if (!spec.validatePostconditions(null, arr, len)) {
            throw new IllegalStateException("Postconditions failed for insertion sort");
        }
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
            new InsertionSort().sort(copy, copy.length, new InsertionSortSpecification());
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
        System.out.println("Insertion Sort Total Correctness: " + (isCorrect ? "PASSED" : "FAILED"));
    }
}
