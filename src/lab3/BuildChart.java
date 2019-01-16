package lab3;

import javafx.beans.property.SimpleDoubleProperty;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Scanner;


public class BuildChart extends ApplicationFrame {
    static Function function;
    static DataX xValues, xInterpolationValues;
    static DataY yValues, yInterpolatedValues;
    static LagrangeMethod lagrangeMethod;
    static double[] xInterpolated, yInterpolated, xValuesFromData, yValuesFromData;
    static Calculator calculator;
    static Scanner in;

    public BuildChart(final String title, DataX xValues, DataY yValues, double[] xInterpolated, double[] yInterpolated) {
        super(title);
        final XYDataset data = createDataset(xValues, yValues, xInterpolated, yInterpolated);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );


        final XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);
        renderer.setSeriesShape(1, new Ellipse2D.Double(0, 0, 0, 0));
        renderer.setSeriesShape(0, new Ellipse2D.Double(0, 0, 6, 6));

        final NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setAutoRangeIncludesZero(false);
        axis.setAutoRangeMinimumSize(1.0);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);

    }
    public BuildChart(final String title, double[] xValues, double[] yValues, double[] xInterpolated, double[] yInterpolated) {
        super(title);
        final XYDataset data = createDataset(xValues, yValues, xInterpolated, yInterpolated);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );


        final XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);
        renderer.setSeriesShape(1, new Ellipse2D.Double(0, 0, 0, 0));
        renderer.setSeriesShape(0, new Ellipse2D.Double(0, 0, 6, 6));

        final NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setAutoRangeIncludesZero(false);
        axis.setAutoRangeMinimumSize(1.0);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);

    }


    public static void main(final String[] args) {
        init();
        buildChart();
        double newXElement = 0;
        boolean fault;
        in.nextLine();
        while (true) {
            do {
                System.out.println("Type new X to calculate");
                while (!in.hasNextDouble()) {
                    System.out.println("You have to type in integer value");
                    in.next();
                }

                try {
                    newXElement = in.nextDouble();
                    fault = false;
                } catch (Exception e) {
                    fault = true;
                }
            } while (fault);
            System.out.println("Interpolated value:");
            System.out.println(lagrangeMethod.interpolateLagrange(newXElement, xValuesFromData, yValuesFromData, xValuesFromData.length));


        }


    }

    private static void buildChart() {
        final BuildChart demo = new BuildChart("Lagrange", xValues, yValues, xInterpolated, yInterpolated);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

    private XYDataset createDataset(DataX xV, DataY yV, double[] xInt, double[] yInt) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        double[] x = xV.getX();
        double[] y = yV.getY();
        int size = x.length;
        final XYSeries series = new XYSeries("Function");
        for (int i = 0; i < size; i++) {
            series.add(x[i], y[i]);
        }
        final XYSeries seriesInterpolated = new XYSeries("Interpolated Data");
        for (int i = 0; i < xInt.length; i++) {
            seriesInterpolated.add(xInt[i], yInt[i]);
        }

        dataset.addSeries(series);
        dataset.addSeries(seriesInterpolated);
        return dataset;
    }

    private XYDataset createDataset(double[] xV, double[] yV, double[] xInt, double[] yInt) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        double[] x = xV;
        double[] y = yV;
        int size = x.length;
        final XYSeries series = new XYSeries("Function");
        for (int i = 0; i < size; i++) {
            series.add(x[i], y[i]);
        }
        final XYSeries seriesInterpolated = new XYSeries("Interpolated Data");
        for (int i = 0; i < xInt.length; i++) {
            seriesInterpolated.add(xInt[i], yInt[i]);
        }

        dataset.addSeries(series);
        dataset.addSeries(seriesInterpolated);
        return dataset;
    }




    private static class Calculator {
        double[] calcYValues(double[] xValues) {
            double[] yValues = new double[xValues.length];
            for (int i = 0; i < xValues.length; i++) {
                yValues[i] = function.calculateFunction(xValues[i]);
            }
            return yValues;
        }
    }


    private static void init() {
        function = new Function();
        calculator = new Calculator();
        xValues = new DataX(generateX(chooseDataSet()));

        double[] yCounted = calculator.calcYValues(DataX.getX());
        yValues = new DataY(yCounted);

        lagrangeMethod = new LagrangeMethod();


        xValuesFromData = xValues.getX();
        yValuesFromData = yValues.getY();

        double xValuesMin = Arrays.stream(xValuesFromData).min().getAsDouble();
        double xValueMax = Arrays.stream(xValuesFromData).max().getAsDouble();
        int iterationsAmount = (int) (xValueMax - xValuesMin) * 10 + 1;
        xInterpolated = new double[iterationsAmount];
        yInterpolated = new double[iterationsAmount];

        for (int i = 0; i < iterationsAmount; i++) {
            xInterpolated[i] = xValuesMin + ((double) (i) / 10);
        }

        for (int i = 0; i < iterationsAmount; i++) {
            yInterpolated[i] = lagrangeMethod.interpolateLagrange(xInterpolated[i], xValuesFromData, yValuesFromData, xValuesFromData.length);
        }
    }

    private static double[] generateX(int size) {
        double[] x = new double[size];
        System.out.println("Generating dataset. X set: ");
        for (int i = 0; i < size; i++) {
            x[i] = i + 1;
            System.out.printf(x[i] + ", ");
        }
        System.out.println();
        return x;
    }

    private static int chooseDataSet() {
        int size;
        in = new Scanner(System.in);
        do {
            System.out.println("Type your dataset size");
            while (!in.hasNextInt()) {
                System.out.println("You have to type in integer value");
                in.next();
            }
            size = in.nextInt();
        } while (Double.isNaN(size));
        return size;
    }

}
