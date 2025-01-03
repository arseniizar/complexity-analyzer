package specifications.sorting;

import specifications.AlgorithmSpecification;

public interface SortingSpecification extends AlgorithmSpecification {
    @Override
    default boolean validatePreconditions(Object... args) {
        if (args.length == 0 || !(args[0] instanceof int[])) {
            return false;
        }
        return true;
    }

    @Override
    default boolean validatePostconditions(Object result, Object... args) {
        if (!(args[0] instanceof int[])) {
            return false;
        }
        int[] sorted = (int[]) args[0];

        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] < sorted[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
