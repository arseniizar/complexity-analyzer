package utils;

import java.util.Arrays;
import java.util.function.Function;

public class TotalCorrectnessChecker {

    public static <Input, Output> boolean checkCorrectness(
            Function<Input, Output> algorithm,
            Function<Input, Output> expected,
            Iterable<Input> testCases
    ) {
        for (Input testCase : testCases) {
            try {
                Output actualOutput = algorithm.apply(testCase);
                Output expectedOutput = expected.apply(testCase);

                if (!areEqual(actualOutput, expectedOutput)) {
                    System.err.println("Test case failed: " + formatValue(testCase));
                    System.err.println("Expected: " + formatValue(expectedOutput));
                    System.err.println("Actual: " + formatValue(actualOutput));
                    return false;
                }

            } catch (Exception e) {
                System.err.println("Algorithm threw an exception for test case: " + formatValue(testCase));
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private static boolean areEqual(Object obj1, Object obj2) {
        if (obj1 == null || obj2 == null) {
            return obj1 == obj2;
        }
        if (obj1.getClass().isArray() && obj2.getClass().isArray()) {
            if (obj1 instanceof int[]) {
                return Arrays.equals((int[]) obj1, (int[]) obj2);
            } else if (obj1 instanceof Object[]) {
                return Arrays.deepEquals((Object[]) obj1, (Object[]) obj2);
            }
        }
        return obj1.equals(obj2);
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value.getClass().isArray()) {
            if (value instanceof int[]) {
                return Arrays.toString((int[]) value);
            } else if (value instanceof Object[]) {
                return Arrays.deepToString((Object[]) value);
            }
        }
        return value.toString();
    }
}
