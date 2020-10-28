package physics.geometry;

public class Point2 {
    public double x, y;

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(Point2 rotatePoint, double angle){
        x = (this.x-rotatePoint.x)* Math.cos(angle) - (this.y - rotatePoint.y)*Math.sin(angle) + rotatePoint.x;
        y = (this.x-rotatePoint.x)* Math.sin(angle) + (this.y - rotatePoint.y)*Math.cos(angle) + rotatePoint.y;
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
