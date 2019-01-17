package lab3;

public class DataY {
    private static double y[];

    DataY(double[]y){
        DataY.y = y;
    }

    public static double[] getY() {
        return y;
    }

    public static void setY(double[] y) {
        DataY.y = y;
    }


}
