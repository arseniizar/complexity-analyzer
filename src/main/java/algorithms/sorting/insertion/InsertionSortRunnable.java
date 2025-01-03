package algorithms.sorting.insertion;

import algorithms.sorting.BaseSortingRunnable;
import specifications.sorting.insertion.InsertionSortSpecification;
import specifications.sorting.SortingSpecification;

public class InsertionSortRunnable extends BaseSortingRunnable {
    private final InsertionSort instance;

    public InsertionSortRunnable(InsertionSort instance) {
        this.instance = instance;
    }

    public InsertionSortRunnable(final int[] array, SortingSpecification spec, InsertionSort instance) {
        this.array = array;
        this.spec = spec;
        this.instance = instance;
    }

    @Override
    public Runnable prepareRunnable(int n) {
        prepareArrayForSorting(n);
        return new InsertionSortRunnable(array, new InsertionSortSpecification(), this.instance);
    }

    @Override
    public void run() {
        this.instance.sort(array, array.length, spec);
    }
}
