public class LagrangeMethod {

//    public static double [] yValues = new double[xValues.length];
//
//    LagrangeMethod(){
//        calcYValues();
//    }

//
//    static double function(double x){
//        return x*x + 2*x;
//    }

    double interpolateLagrange (double x, double [] xValues, double []yValues, int size){
        double lagrangePol=0;

        for (int i=0;i<size;i++){
            double basicsPol = 1;
            for (int j=0;j<size;j++){
                if (j!=i){
                    basicsPol*=(x-xValues[j])/(xValues[i]-xValues[j]);
                }
            }

            lagrangePol += basicsPol*yValues[i];
        }

        return lagrangePol;
    }
}
