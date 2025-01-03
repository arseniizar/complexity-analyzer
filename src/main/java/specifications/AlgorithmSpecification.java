package specifications;

public interface AlgorithmSpecification {
    boolean validatePreconditions(Object... args);

    boolean validatePostconditions(Object result, Object... args);
}
