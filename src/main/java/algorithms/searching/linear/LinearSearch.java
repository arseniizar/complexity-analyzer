package algorithms.searching.linear;

import algorithms.Algorithm;
import org.jetbrains.annotations.NotNull;
import specifications.searching.linear.LinearSearchSpecification;
import utils.TotalCorrectnessChecker;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class LinearSearch extends Algorithm {

    public int search(int[] S, int len, int key, @NotNull LinearSearchSpecification spec) {
        dominantOperationCount = 0;
        if (!spec.validatePreconditions(S, len)) {
            throw new IllegalArgumentException("Preconditions not satisfied for linear search");
        }

        for (int i = 0; i < len; i++) {
            dominantOperationCount++;
            if (S[i] == key) {
                if (!spec.validatePostconditions(i, S, key)) {
                    throw new IllegalStateException("Postconditions not satisfied for binary search");
                }
                return i;
            }
        }

        if (!spec.validatePostconditions(-1, S, key)) {
            throw new IllegalStateException("Postconditions not satisfied for binary search");
        }
        return -1;
    }

    @Override
    public void checkCorrectness() {
        LinearSearchSpecification spec = new LinearSearchSpecification();

        List<int[]> testCases = Arrays.asList(
                new int[]{1, 2, 3, 4, 5},      // Simple array
                new int[]{10, 20, 30, 40},    // Another sorted array
                new int[]{100, 200, 300, 400, 500}, // Larger sorted array
                new int[]{7, 2, 9, 4, 6},     // Unsorted array
                new int[]{},                  // Edge case: empty array
                new int[]{42}                 // Edge case: single-element array
        );

        Function<int[], Integer> algorithm = array -> {
            if (array.length == 0) {
                return -1;
            }
            int key = array[array.length / 2];
            return search(array, array.length, key, spec);
        };

        Function<int[], Integer> expected = array -> {
            if (array.length == 0) return -1;
            int key = array[array.length / 2];
            for (int i = 0; i < array.length; i++) {
                if (array[i] == key) {
                    return i;
                }
            }
            return -1;
        };

        boolean isCorrect = TotalCorrectnessChecker.checkCorrectness(algorithm, expected, testCases);

        System.out.println("Linear Search Total Correctness: " + (isCorrect ? "PASSED" : "FAILED"));
    }
}
