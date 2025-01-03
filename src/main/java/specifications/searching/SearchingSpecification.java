package specifications.searching;

import specifications.AlgorithmSpecification;

import java.util.Arrays;

public interface SearchingSpecification extends AlgorithmSpecification {
    @Override
    default boolean validatePreconditions(Object... args) {
        return args.length >= 2 && args[0] instanceof int[] && args[1] instanceof Integer; // Invalid input
    }

    @Override
    default boolean validatePostconditions(Object result, Object... args) {
        if (!(args[0] instanceof int[] array) || !(args[1] instanceof Integer)) {
            System.out.println("Postcondition failed: Invalid arguments.");
            return false;
        }

        int key = (Integer) args[1];
        int index = (result instanceof Integer) ? (Integer) result : -1;

        if (index == -1) {
            // Check that the key does not exist in the array
            boolean keyNotFound = Arrays.stream(array).noneMatch(val -> val == key);
            if (!keyNotFound) {
                System.out.println("Postcondition failed: Key exists in array but was not found.");
            }
            return keyNotFound;
        } else {
            // Check that the key exists at the given index
            boolean correctIndex = index >= 0 && index < array.length && array[index] == key;
            if (!correctIndex) {
                System.out.println("Postcondition failed: Incorrect index " + index + " for key " + key);
                System.out.println("Array[index]: " + array[index]);
            }
            return correctIndex;
        }
    }

}
