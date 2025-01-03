package algorithms.searching;

import algorithms.AlgorithmPreparer;
import specifications.searching.SearchingSpecification;
import specifications.searching.binary.BinarySearchSpecification;

public class BaseSearchingRunnable implements Runnable, AlgorithmPreparer {
    protected int[] array;
    protected int key;
    protected SearchingSpecification spec;

    @Override
    public Runnable prepareRunnable(int n) {
        return null;
    }

    @Override
    public void run() {

    }
}
