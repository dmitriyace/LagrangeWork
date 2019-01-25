package lab4;

import lab3.BuildChart;
import org.jfree.ui.RefineryUtilities;
import java.util.*;

public class AdamsMethod {

    private static Map<Double, Double> map;
    private static boolean firstCycle;
    private static int n, c;
    private static double x0, y0, h, e, xn;
    private static final int MAX_ITERATIONS = 100;
    private static Scanner scanner;
    private static List<Double>
            x = new ArrayList<>(),
            y = new ArrayList<>(),
            dy = new ArrayList<>(),
            yCorr = new ArrayList<>(),
            yp = new ArrayList<>();
    private static int chosenFunction;

    public static void main(String[] args) {
        do
            init();
        while (createNewChart());
    }

    private static void init() {
        getStartingValues();
        adamsMethod();
        double[] xValues = new double[map.size()];
        double[] yValues = new double[map.size()];
        List<Double> x = new ArrayList<>(map.keySet());
        List<Double> y = new ArrayList<>(map.values());
        for (int i = 0; i < map.size(); i++) {
            xValues[i] = x.get(i);
            yValues[i] = y.get(i);
        }
        final BuildChart demo = new BuildChart("Adams method", xValues, yValues, new double[]{}, new double[]{}, "");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

    private static void getStartingValues() {
        try {
            scanner = new Scanner(System.in);
            getFunction();
            if (chosenFunction == 1 || chosenFunction == 2) {
                getParams();
            } else {
                getStartingValues();
            }
        } catch (Exception e) {
            System.out.println("Please, type in correct data");
            getStartingValues();
        }

    }

    private static void getFunction() {
        System.out.println("choose function which will be calculated\n(type 1 or 2)\n1) 2 * (x * x + y)\n2) 0.5 * Math.sin(x) + 2 - y * y");
        try {
            chosenFunction = Integer.parseInt(scanner.nextLine());
        } catch (Exception ex) {
            getFunction();
        }
    }

    private static void getParams() {
        System.out.println("Type in four parameters:\nx0, y0, last x and accuracy \nusing space as delimiter");
        String[] answer = scanner.nextLine().replace(",", ".").split(" ");
        if (answer.length == 4) {
            x0 = Double.parseDouble(answer[0]);
            y0 = Double.parseDouble(answer[1]);
            xn = Double.parseDouble(answer[2]);
            e = Double.parseDouble(answer[3]);
        } else {
            getParams();
        }
    }

    private static Map<Double, Double> adamsMethod() {
        firstCycle = true;
        cycle();
        map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(x0 + i * h, y.get(i));
        }
        return map;
    }

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
        getStartingValuesByEuler();

        for (int i = 0; i < 4; i++) {
            yp.add(calcFunction(x0 + i * h, y.get(i)));
        }

        for (int i = 4; i < n; i++) {
            y.add(y.get(i - 1) + adamsPredictor(i - 1));
            yp.add(calcFunction(x0 + i * h, y.get(i)));
            double yCorr = y.get(i - 1) + adamsCorrector(i);
            if (Math.abs(y.get(y.size() - 1) - yCorr) > e && c < MAX_ITERATIONS) {
                cycle();
            }
            y.set(i, yCorr);
            yp.set(i, calcFunction(x0 + i * h, yCorr));
        }
    }

    private static double adamsCorrector(int i) {
        return h / 24 * (9 * yp.get(i) + 19 * yp.get(i - 1) - 5 * yp.get(i - 2) + yp.get(i - 3));
    }

    private static double adamsPredictor(int i) {
        return h / 24 * (55 * yp.get(i) - 59 * yp.get(i - 1) + 37 * yp.get(i - 2) - 9 * yp.get(i - 3));
    }


    private static void getStartingValuesByEuler() {
        for (int i = 1; i <= 4; i++) {
            y.add(y.get(i - 1) + h * calcFunction(x0 + (i - 1) * h, y.get(i - 1)));
        }
    }

    private static double calcFunction(double x, double y) {
        switch (chosenFunction) {
            case 1:
                return 2 * (x * x + y);
            case 2:
                return 0.5 * Math.sin(x) + 2 - y * y;
            default:
                getStartingValues();
                return 0;
        }
    }



    private static boolean createNewChart() {
        String answer = "";
        System.out.println("Whould you like to make a new chart?(type \"y\" to create new chart)\nor would you like to stop the program(type \"q\" to stop)");
        scanner.reset();
        answer = scanner.nextLine();
        if (answer.equals("y")) {
            return true;
        } else if (answer.equals("q")) {
            System.exit(0);
        } else {
            System.out.println("You should type y or n!");
            createNewChart();
        }
        return false;
    }

}
