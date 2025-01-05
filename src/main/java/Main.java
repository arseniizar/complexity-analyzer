import algorithms.searching.binary.BinarySearch;
import algorithms.searching.linear.LinearSearch;
import algorithms.sorting.insertion.InsertionSort;
import algorithms.sorting.merge.MergeSort;
import algorithms.sorting.selection.SelectionSort;
import utils.AlgorithmExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<Class<?>> algorithms = new ArrayList<>();

        algorithms.add(BinarySearch.class);
        algorithms.add(LinearSearch.class);
        algorithms.add(SelectionSort.class);
        algorithms.add(InsertionSort.class);
        algorithms.add(MergeSort.class);

        System.out.println("Choose an algorithm to run:");
        for (int i = 0; i < algorithms.size(); i++) {
            System.out.println((i + 1) + ". " + algorithms.get(i).getSimpleName());
        }
        System.out.print("Enter your choice (1 to " + algorithms.size() + "): ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.close();

        if (choice < 1 || choice > algorithms.size()) {
            System.out.println("Invalid choice! Please restart the program and enter a valid number.");
            return;
        }

        AlgorithmExecutor.warmUp();

        Class<?> chosenAlgorithm = algorithms.get(choice - 1);
        System.out.println("Running " + chosenAlgorithm.getSimpleName() + "...");

        switch (choice) {
            case 1 -> {
                AlgorithmExecutor.runBinarySearch();
            }
            case 2 -> {
                AlgorithmExecutor.runLinearSearch();
            }
            case 3 -> {
                AlgorithmExecutor.runSelectionSort();
            }
            case 4 -> {
                AlgorithmExecutor.runInsertionSort();
            }
            case 5 -> {
                AlgorithmExecutor.runMergeSort();
            }
            default -> System.out.println("Invalid choice! Please restart the program and enter 1 or 2.");
        }
    }
}
