public class DataX {
    private static double[] x;

    DataX(double[]x){
        DataX.setX(x);
    }

    public static double[] getX() {
        return x;
    }

    public static void setX(double[] x) {
        DataX.x = x;
    }
}
