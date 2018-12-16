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

import java.util.Arrays;


public class BuildChart extends ApplicationFrame {
    static Function function;
    static DataX xValues, xInterpolationValues;
    static DataY yValues, yInterpolatedValues;
    static LagrangeMethod lagrangeMethod;
    static double[] xInterpolated, yInterpolated, xValuesFromData, yValuesFromData;
    static Calculator calculator;
    public BuildChart(final String title, DataX xValues, DataY yValues, double[] xInterpolated, double[] yInterpolated) {
        super(title);



        final XYDataset data = createDataset(xValues,yValues,xInterpolated,yInterpolated);

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
        final NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setAutoRangeIncludesZero(false);
        axis.setAutoRangeMinimumSize(1.0);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseLinesVisible(true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);

    }



    public static void main(final String[] args) {

        init();

        final BuildChart demo = new BuildChart("Lagrange", xValues, yValues, xInterpolated, yInterpolated);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

    private XYDataset createDataset(DataX xV, DataY yV,double[] xInt, double []yInt){
        XYSeriesCollection dataset = new XYSeriesCollection();
        double[] x = xV.getX();
        double[] y = yV.getY();
        int size = x.length;
        final XYSeries series = new XYSeries("Function");
        for (int i = 0; i < size; i++) {
            series.add(x[i], y[i]);
        }
        final XYSeries seriesInterpolated = new XYSeries("Interpolated Data");
        for (int i = 0; i<xInt.length;i++ ){
            seriesInterpolated.add(xInt[i],yInt[i]);
        }
        dataset.addSeries(series);
        dataset.addSeries(seriesInterpolated);
        return  dataset;
    }



    static class Calculator {
        double[] calcYValues(double[] xValues) {
            double[] yValues = new double[xValues.length];
            for (int i = 0; i < xValues.length; i++) {
                yValues[i] = function.calculateFunction(xValues[i]);
//                System.out.println(function.calculateFunction(xValues[i]));
            }
            return yValues;
        }
    }

    static void init(){
        function = new Function();
        calculator = new Calculator();
        xValues = new DataX(generateX(60));

        double[] yCounted = calculator.calcYValues(DataX.getX());
        yValues = new DataY(yCounted);
//        double[]yHardcode = {3,7,10,12,20,38,52,63,78,101,110,140,160,187,230,245};
//        yValues = new DataY(yHardcode);

        lagrangeMethod = new LagrangeMethod();
//        System.out.println(lagrangeMethod.interpolateLagrange(0.3, xValues.getX(), yValues.getY(), 4));


        xValuesFromData = xValues.getX();
        yValuesFromData = yValues.getY();

        double xValuesMin = Arrays.stream(xValuesFromData).min().getAsDouble();
        double xValueMax = Arrays.stream(xValuesFromData).max().getAsDouble();
        int iterationsAmount = (int) (xValueMax - xValuesMin) * 10+1;
        xInterpolated = new double[iterationsAmount];
        yInterpolated = new double[iterationsAmount];

        for (int i = 0; i < iterationsAmount; i++) {
            xInterpolated[i] = xValuesMin + ((double)(i)/10);
//            System.out.println((double)i/100);
        }

        for (int i = 0; i < iterationsAmount; i++) {
            yInterpolated[i] = lagrangeMethod.interpolateLagrange(xInterpolated[i], xValuesFromData, yValuesFromData, xValuesFromData.length);
            System.out.println(yInterpolated[i]);
        }
    }
    private static double[]generateX(int size){
        double[]x = new double[size];
        for (int i=0;i<size;i++){
            x[i] = i+1;
            System.out.println(x[i]);
        }
        return x;
    }
}