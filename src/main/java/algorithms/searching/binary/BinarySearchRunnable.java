package algorithms.searching.binary;

import algorithms.searching.BaseSearchingRunnable;
import specifications.searching.binary.BinarySearchSpecification;

import java.util.Arrays;

public class BinarySearchRunnable extends BaseSearchingRunnable {
    private final BinarySearch instance;

    public BinarySearchRunnable(BinarySearch instance) {
        this.instance = instance;
    }

    public BinarySearchRunnable(int[] array, int key, BinarySearchSpecification spec, BinarySearch instance) {
        this.array = array;
        this.key = key;
        this.spec = spec;
        this.instance = instance;
    }

    @Override
    public void run() {
        instance.search(array, array.length, key, (BinarySearchSpecification) spec);
    }

    @Override
    public Runnable prepareRunnable(int n) {
        long prepStartTime = System.nanoTime();

        // Array preparation
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }
        Arrays.sort(array);

        long prepEndTime = System.nanoTime();
        System.out.println("Array preparation time for input size " + n + ": "
                + ((prepEndTime - prepStartTime) / 1e6) + " ms");

        return new BinarySearchRunnable(array, n / 2, new BinarySearchSpecification(), this.instance);
    }
}
