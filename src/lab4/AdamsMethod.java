package lab4;

import lab3.BuildChart;
import lab3.LagrangeMethod;
import org.jfree.ui.RefineryUtilities;

import java.util.*;

public class AdamsMethod {

    static Map<Double, Double> map;
    static boolean firstCycle = true;
    static int n, c;
    static double x0, y0, h, e, xn;
    private static final int MAX_ITERATIONS = 100000;
    static List<Double>
            x = new ArrayList<Double>(),
            y = new ArrayList<Double>(),
            dy = new ArrayList<Double>(),
            yCorr = new ArrayList<Double>(),
            yp = new ArrayList<Double>();

    private static void cycle() {
        if (firstCycle) {
            firstCycle = false;
            c = 0;
            n = 10;
            h = (xn - x0) / n;
        } else {
            n *= 2;
            h /= 2;
        }
        c++;
        dy.clear();
        x.clear();
        y.clear();
        yCorr.clear();
        yp.clear();

        y.add(y0);
        euler(1, 4);

        for (int i = 0; i < 4; i++) {
            yp.add(calcFunction(x0 + i * h, y.get(i)));
        }

        for (int i = 4; i < n; i++) {
            y.add(y.get(i - 1) + adams(i - 1));
            yp.add(calcFunction(x0 + i * h, y.get(i)));
            double yCorr = y.get(i - 1) + adamsCorr(i);
            if (Math.abs(y.get(y.size() - 1) - yCorr) > e && c < MAX_ITERATIONS) {
                cycle();
            }
            y.set(i, yCorr);
            yp.set(i, calcFunction(x0 + i * h, yCorr));

        }


    }

    private static double adamsCorr(int i) {
        return h / 24 * (9 * yp.get(i) + 19 * yp.get(i - 1) - 5 * yp.get(i - 2) + yp.get(i - 3));
    }

    private static double adams(int i) {
        return h / 24 * (55 * yp.get(i) - 59 * yp.get(i - 1) + 37 * yp.get(i - 2) - 9 * yp.get(i - 3));
    }


    private static void euler(int a, int b) {
        for (int i = a; i <= b; i++) {
            y.add(y.get(i - 1) + h * calcFunction(x0 + (i - 1) * h, y.get(i - 1)));
        }
    }

    private static double calcFunction(double x, double y) {
        return 0.5 * Math.sin(x)  + 2 - y*y;
    }

    private static Map<Double, Double> adamsMethod() {
        cycle();
        map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(x0 + i * h, y.get(i));
        }
        return map;
    }

    public static void main(String[] args) {
        getStartValues();
        System.out.println(adamsMethod().size());
        double[] xValues = new double[map.size()];
        double[] yValues = new double[map.size()];
        List<Double> x = new ArrayList<Double>(map.values());
        List<Double> y = new ArrayList<Double>(map.keySet());

        for (int i = 0; i < map.size(); i++) {
            xValues[i] = x.get(i);
        }
        for (int i = 0; i < map.size(); i++) {
            yValues[i] = y.get(i);
        }

        LagrangeMethod lagrangeMethod = new LagrangeMethod();

        double[] xValuesFromData = xValues;
        double[] yValuesFromData = yValues;

        double xValuesMin;

        xValuesMin = Arrays.stream(xValuesFromData).min().getAsDouble();
        double xValueMax = Arrays.stream(xValuesFromData).max().getAsDouble();
        int iterationsAmount = (int) (xValueMax - xValuesMin) * 10 + 1;
        double[] xInterpolated = new double[iterationsAmount];
        double[] yInterpolated = new double[iterationsAmount];

        for (int i = 0; i < iterationsAmount; i++) {
            xInterpolated[i] = xValuesMin + ((double) (i) / 10);
        }

        for (int i = 0; i < iterationsAmount; i++) {
            yInterpolated[i] = lagrangeMethod.interpolateLagrange(xInterpolated[i], xValuesFromData, yValuesFromData, xValuesFromData.length);
        }

        final BuildChart demo = new BuildChart("Adams method", xValues, yValues, xInterpolated, yInterpolated);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
//        new BuildChart(xValues, yValues, )

    }

    private static void getStartValues() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type in x0, y0, last x and accuracy using space as delimiter");
            String[] answer = scanner.nextLine().replace(",", ".").split(" ");
            x0 = Double.parseDouble(answer[0]);
            y0 = Double.parseDouble(answer[1]);
            xn = Double.parseDouble(answer[2]);
            e = Double.parseDouble(answer[3]);

        } catch (Exception e) {
            getStartValues();

        }
    }
}

