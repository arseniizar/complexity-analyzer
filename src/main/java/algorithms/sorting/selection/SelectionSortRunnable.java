package algorithms.sorting.selection;

import algorithms.sorting.BaseSortingRunnable;
import specifications.sorting.selection.SelectionSortSpecification;
import specifications.sorting.SortingSpecification;

public class SelectionSortRunnable extends BaseSortingRunnable {
    private final SelectionSort instance;

    public SelectionSortRunnable(SelectionSort instance) {
        this.instance = instance;
    }

    public SelectionSortRunnable(final int[] S, SortingSpecification spec, SelectionSort instance) {
        this.array = S;
        this.spec = spec;
        this.instance = instance;
    }

    @Override
    public Runnable prepareRunnable(int n) {
        prepareArrayForSorting(n);
        return new SelectionSortRunnable(array, new SelectionSortSpecification(), this.instance);
    }

    @Override
    public void run() {
        instance.sort(array, array.length, spec);
    }
}

