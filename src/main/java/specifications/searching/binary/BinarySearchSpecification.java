package specifications.searching.binary;

import specifications.searching.SearchingSpecification;

public class BinarySearchSpecification implements SearchingSpecification {

    @Override
    public boolean validatePreconditions(Object... args) {
        // Ensure sufficient arguments are passed
        if (args.length < 2 || !(args[0] instanceof int[] array) || !(args[1] instanceof Integer)) {
            return false; // Invalid input
        }

        // Check if the array is sorted
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false; // Array is not sorted
            }
        }

        return true; // Preconditions satisfied
    }
}
