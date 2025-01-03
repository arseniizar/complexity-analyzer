package utils;

import algorithms.Algorithm;
import algorithms.AlgorithmPreparer;
import algorithms.searching.binary.BinarySearch;
import algorithms.searching.binary.BinarySearchRunnable;
import algorithms.searching.linear.LinearSearch;
import algorithms.searching.linear.LinearSearchRunnable;
import algorithms.sorting.insertion.InsertionSort;
import algorithms.sorting.insertion.InsertionSortRunnable;
import algorithms.sorting.selection.SelectionSort;
import algorithms.sorting.selection.SelectionSortRunnable;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import specifications.AlgorithmSpecification;
import specifications.searching.binary.BinarySearchSpecification;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class ComplexityAnalyzer {
    public static AlgorithmSpecification currentSpecification;

    public static void main(String[] args) {
        List<Class<?>> algorithms = new ArrayList<>();

        algorithms.add(BinarySearch.class);
        algorithms.add(LinearSearch.class);
        algorithms.add(SelectionSort.class);
        algorithms.add(InsertionSort.class);

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

        Class<?> chosenAlgorithm = algorithms.get(choice - 1);
        System.out.println("Running " + chosenAlgorithm.getSimpleName() + "...");

        switch (choice) {
            case 1 -> {
                runBinarySearch();
            }
            case 2 -> {
                runLinearSearch();
            }
            case 3 -> {
                runSelectionSort();
            }
            case 4 -> {
                runInsertionSort();
            }
            default -> System.out.println("Invalid choice! Please restart the program and enter 1 or 2.");
        }
    }


    private static void runSelectionSort() {
        int[] inputSizes = {1, 10, 100};
        SelectionSort selectionSort = new SelectionSort();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new SelectionSortRunnable(selectionSort), selectionSort);
    }

    private static void runBinarySearch() {
        int[] inputSizes = {100, 1000, 10000, 100000};
        BinarySearch binarySearch = new BinarySearch();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new BinarySearchRunnable(binarySearch), binarySearch);
    }


    private static void runLinearSearch() {
        int[] inputSizes = {100, 1000, 10000, 100000};
        LinearSearch linearSearch = new LinearSearch();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new LinearSearchRunnable(linearSearch), linearSearch);
    }

    private static void runInsertionSort() {
        int[] inputSizes = {100, 1000, 2000};
        InsertionSort insertionSort = new InsertionSort();
        ComplexityAnalyzer.analyzeAlgorithm(inputSizes, new InsertionSortRunnable(insertionSort), insertionSort);
    }

    public static void analyzeAlgorithm(int[] inputSizes, AlgorithmPreparer preparer, Algorithm algorithmClass) {
        List<Double> times = new ArrayList<>();
        List<Integer> dominantOperations = new ArrayList<>();

        for (int n : inputSizes) {
            analyzeSingleInputSize(n, preparer, times, dominantOperations, algorithmClass);
        }

        printAnalysisResults(inputSizes, times, dominantOperations);

        checkAlgorithmCorrectness(algorithmClass);

        PolynomialFunction fittedFunction = analyzeComplexity(inputSizes, dominantOperations);
        plotResults(inputSizes, dominantOperations, fittedFunction, algorithmClass);

        currentSpecification = null;
    }


    private static void checkAlgorithmCorrectness(Algorithm algorithm) {
        if (algorithm == null) {
            throw new IllegalStateException("Algorithm class is not set.");
        }

        try {
            algorithm.checkCorrectness();
        } catch (UnsupportedOperationException e) {
            System.out.println("Correctness check not implemented for: " + algorithm.getClass().getSimpleName());
        }
    }

    private static void analyzeSingleInputSize(int inputSize, AlgorithmPreparer preparer, List<Double> times, List<Integer> dominantOperations, Algorithm algorithmClass) {
        Runnable algorithm = preparer.prepareRunnable(inputSize);

        long totalTime = 0;
        int iterations = (inputSize > 5000) ? 1000 : 10_000;
        int totalDominantOperations = 0;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            algorithm.run();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);

            totalDominantOperations += getDominantOperationCount(algorithmClass);
        }

        double avgTime = Math.max(0, ((double) totalTime / iterations) / 1e6);
        times.add(avgTime);
        dominantOperations.add(totalDominantOperations / iterations);
    }

    private static int getDominantOperationCount(@NotNull Algorithm algorithm) {
        return algorithm.dominantOperationCount;
    }

    private static void printAnalysisResults(int[] inputSizes, List<Double> times, List<Integer> dominantOperations) {
        System.out.println("Input Sizes: " + Arrays.toString(inputSizes));
        System.out.println("Execution Times (ms): " + times);
        System.out.println("Dominant Operation Counts: " + dominantOperations);
    }


    private static PolynomialFunction analyzeComplexity(int[] inputSizes, List<Integer> dominantOperations) {
        String[] models = {"O(1)", "O(log n)", "O(n)", "O(n log n)", "O(n^2)"};
        List<Function<Double, Double>> transformations = getTransformations();

        String bestModel = null;
        PolynomialFunction bestFitFunction = null;
        double bestError = Double.MAX_VALUE;

        for (int i = 0; i < models.length; i++) {
            WeightedObservedPoints transformedPoints = transformData(inputSizes, dominantOperations, transformations.get(i));

            try {
                PolynomialFunction function = fitModel(transformedPoints);
                double error = computeError(transformedPoints, function);

                if (error < bestError) {
                    bestError = error;
                    bestModel = models[i];
                    bestFitFunction = function;
                }
            } catch (Exception e) {
                System.err.println("Error fitting model " + models[i] + ": " + e.getMessage());
            }
        }

        printBestFitResult(bestModel, bestFitFunction);
        return bestFitFunction;
    }

    private static List<Function<Double, Double>> getTransformations() {
        return Arrays.asList(
                (Double x) -> 1.0, // O(1)
                (Double x) -> Math.log(x + 1) / Math.log(2), // O(log n)
                (Double x) -> x, // O(n)
                (Double x) -> x * Math.log(x) / Math.log(2), // O(n log n)
                (Double x) -> x * x // O(n^2)
        );
    }

    private static WeightedObservedPoints transformData(int[] inputSizes, List<Integer> dominantOperations, Function<Double, Double> transformation) {
        WeightedObservedPoints transformedPoints = new WeightedObservedPoints();
        for (int j = 0; j < inputSizes.length; j++) {
            double transformedX = transformation.apply((double) inputSizes[j]);
            double y = dominantOperations.get(j); // Use dominant operation counts
            transformedPoints.add(transformedX, y);
        }
        return transformedPoints;
    }

    private static PolynomialFunction fitModel(WeightedObservedPoints points) {
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2); // Linear model
        double[] coeffs = fitter.fit(points.toList());
        return new PolynomialFunction(coeffs);
    }

    private static void printBestFitResult(String bestModel, PolynomialFunction bestFitFunction) {
        System.out.println("\nBest Fit Model: " + bestModel);
        System.out.println("Fitted Function: " + bestFitFunction);
    }


    private static double computeError(WeightedObservedPoints points, PolynomialFunction function) {
        double error = 0.0;
        for (WeightedObservedPoint point : points.toList()) {
            double predicted = function.value(point.getX());
            double actual = point.getY();
            error += Math.pow(predicted - actual, 2);
        }
        return Math.sqrt(error); // Return root mean square error (RMSE)
    }

    private static void plotResults(int[] inputSizes, List<Integer> comparisons, PolynomialFunction fittedFunction, Algorithm algorithm) {
        XYSeries actualDataSeries = new XYSeries("Actual Data");
        XYSeries fittedCurveSeries = new XYSeries("Fitted Curve");

        for (int i = 0; i < inputSizes.length; i++) {
            actualDataSeries.add(inputSizes[i], comparisons.get(i));
        }

        int minInputSize = Arrays.stream(inputSizes).min().orElse(1);
        int maxInputSize = Arrays.stream(inputSizes).max().orElse(100);

        for (int n = minInputSize; n <= maxInputSize; n += Math.max(1, (maxInputSize - minInputSize) / 100)) {
            double logValue = Math.log(n + 1) / Math.log(2);
            double value = Math.max(0, fittedFunction.value(logValue));
            fittedCurveSeries.add(n, value);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(actualDataSeries);
        dataset.addSeries(fittedCurveSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                algorithm.getClass().getSimpleName() + " Complexity",
                "Input Size (n)",
                "Dominant Operation Counts",
                dataset
        );

        JFrame chartFrame = new JFrame();
        chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chartFrame.setTitle("Algorithm Complexity Plot");
        chartFrame.setSize(800, 600);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartFrame.setContentPane(chartPanel);

        chartFrame.setVisible(true);
    }


    private static void warmUp() {
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
