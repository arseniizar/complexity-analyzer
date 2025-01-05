package algorithms.sorting.merge;

import algorithms.sorting.BaseSortingRunnable;
import specifications.sorting.merge.MergeSortSpecification;

public class MergeSortRunnable extends BaseSortingRunnable {
    private final MergeSort instance;

    public MergeSortRunnable(MergeSort instance) {
        this.instance = instance;
    }

    public MergeSortRunnable(int[] S, MergeSortSpecification spec, MergeSort instance) {
        this.instance = instance;
        this.array = S;
        this.spec = spec;
    }

    @Override
    public void run() {
        this.instance.sort(this.array, this.array.length, this.spec);
    }

    @Override
    public Runnable prepareRunnable(int n) {
        prepareArrayForSorting(n);
        return new MergeSortRunnable(this.array, new MergeSortSpecification(), this.instance);
    }
}
