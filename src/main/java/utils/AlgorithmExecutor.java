package utils;

import algorithms.searching.binary.BinarySearch;
import algorithms.searching.binary.BinarySearchRunnable;
import algorithms.searching.linear.LinearSearch;
import algorithms.searching.linear.LinearSearchRunnable;
import algorithms.sorting.insertion.InsertionSort;
import algorithms.sorting.insertion.InsertionSortRunnable;
import algorithms.sorting.merge.MergeSort;
import algorithms.sorting.merge.MergeSortRunnable;
import algorithms.sorting.selection.SelectionSort;
import algorithms.sorting.selection.SelectionSortRunnable;
import specifications.searching.binary.BinarySearchSpecification;

public class AlgorithmExecutor {

    public static void runSelectionSort() {
        int[] inputSizes = {1, 10, 100};
        SelectionSort selectionSort = new SelectionSort();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new SelectionSortRunnable(selectionSort), selectionSort);
    }

    public static void runBinarySearch() {
        int[] inputSizes = {100, 1000, 10000, 100000};
        BinarySearch binarySearch = new BinarySearch();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new BinarySearchRunnable(binarySearch), binarySearch);
    }


    public static void runLinearSearch() {
        int[] inputSizes = {100, 1000, 10000, 100000};
        LinearSearch linearSearch = new LinearSearch();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new LinearSearchRunnable(linearSearch), linearSearch);
    }

    public static void runInsertionSort() {
        int[] inputSizes = {100, 1000, 2000};
        InsertionSort insertionSort = new InsertionSort();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new InsertionSortRunnable(insertionSort), insertionSort);
    }

    public static void runMergeSort() {
        int[] inputSizes = {10, 100, 1000};
        MergeSort mergeSort = new MergeSort();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new MergeSortRunnable(mergeSort), mergeSort);
    }

    public static void warmUp() {
        System.out.println("Warming up the JVM...");
        int[] dummyArray = new int[100];
        for (int i = 0; i < dummyArray.length; i++) {
            dummyArray[i] = i;
        }

        int warmUpKey = 50;

        for (int i = 0; i < 10_000; i++) {
            new BinarySearch().search(dummyArray, dummyArray.length, warmUpKey, new BinarySearchSpecification());
        }
        System.out.println("JVM warm-up completed.");
    }
}
