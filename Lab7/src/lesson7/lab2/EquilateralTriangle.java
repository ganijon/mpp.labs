package lesson7.lab2;

public class EquilateralTriangle implements Polygon {

    private double side;

    public EquilateralTriangle(double side) {
        this.side = side;
    }

    @Override
    public double[] getLengths() {
        return new double[]{side, side, side};
    }

  
}