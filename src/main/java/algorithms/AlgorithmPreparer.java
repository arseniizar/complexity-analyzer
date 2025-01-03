package algorithms;

@FunctionalInterface
public interface AlgorithmPreparer {
    Runnable prepareRunnable(int n);
}
