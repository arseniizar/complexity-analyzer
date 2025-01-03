package algorithms.sorting;

import algorithms.AlgorithmPreparer;
import specifications.sorting.SortingSpecification;

import java.util.Random;

public class BaseSortingRunnable implements Runnable, AlgorithmPreparer {
    protected int[] array;
    protected SortingSpecification spec;

    @Override
    public void run() {
    }

    @Override
    public Runnable prepareRunnable(int n) {
        return null;
    }

    public void prepareArrayForSorting(int n) {
        long prepStartTime = System.nanoTime();

        this.array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            this.array[i] = random.nextInt(10_000);
        }

        long prepEndTime = System.nanoTime();
        System.out.println("Array preparation time for input size " + n + ": "
                + ((prepEndTime - prepStartTime) / 1e6) + " ms");
    }
}
