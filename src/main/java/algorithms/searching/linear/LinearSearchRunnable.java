package algorithms.searching.linear;

import algorithms.searching.BaseSearchingRunnable;
import specifications.searching.linear.LinearSearchSpecification;

import java.util.Random;

public class LinearSearchRunnable extends BaseSearchingRunnable {
    private final LinearSearch instance;

    public LinearSearchRunnable(LinearSearch instance) {
        this.instance = instance;
    }

    public LinearSearchRunnable(int[] array, int key, LinearSearchSpecification spec, LinearSearch instance) {
        this.instance = instance;
        this.array = array;
        this.key = key;
        this.spec = spec;
    }

    @Override
    public Runnable prepareRunnable(int n) {

        long prepStartTime = System.nanoTime();

        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(10_000);
        }

        long prepEndTime = System.nanoTime();
        System.out.println("Array preparation time for input size " + n + ": "
                + ((prepEndTime - prepStartTime) / 1e6) + " ms");

        return new LinearSearchRunnable(array, n / 2, new LinearSearchSpecification(), this.instance);
    }

    @Override
    public void run() {
        this.instance.search(array, array.length, key, (LinearSearchSpecification) spec);
    }
}
