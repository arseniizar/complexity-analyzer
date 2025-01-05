package utils;

import algorithms.Algorithm;
import algorithms.AlgorithmPreparer;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import specifications.AlgorithmSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ComplexityAnalyzer {
    public static AlgorithmSpecification currentSpecification;

    public static void analyzeAlgorithm(int[] inputSizes, AlgorithmPreparer preparer, Algorithm algorithmClass) {
        List<Double> times = new ArrayList<>();
        List<Integer> dominantOperations = new ArrayList<>();

        for (int n : inputSizes) {
            analyzeSingleInputSize(n, preparer, times, dominantOperations, algorithmClass);
        }

        printAnalysisResults(inputSizes, times, dominantOperations);

        checkAlgorithmCorrectness(algorithmClass);

        PolynomialFunction fittedFunction = analyzeComplexity(inputSizes, dominantOperations);
        AlgorithmVisualizer.plotResults(inputSizes, dominantOperations, fittedFunction, algorithmClass);

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

    private static void analyzeSingleInputSize(int inputSize, AlgorithmPreparer preparer, List<Double> times, List<Integer> dominantOperations, Algorithm algorithm) {
        Runnable algorithmRunnable = preparer.prepareRunnable(inputSize);

        long totalTime = 0;
        int iterations = (inputSize > 5000) ? 1000 : 10_000;
        int totalDominantOperations = 0;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            algorithmRunnable.run();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);

            totalDominantOperations += getDominantOperationCount(algorithm);
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
}
