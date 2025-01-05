package utils;

import algorithms.Algorithm;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class AlgorithmVisualizer {
    public static void plotResults(int[] inputSizes, List<Integer> comparisons, PolynomialFunction fittedFunction, Algorithm algorithm) {
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

        algorithm.visualize();
        chartFrame.setVisible(true);
    }
}
