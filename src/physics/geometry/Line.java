package physics.geometry;

import physics.drawing.Drawable;
import physics.utils.Tools;

import java.awt.*;

public class Line implements Drawable {
    public double x1, x2, y1, y2;
    private double k, b;
    private double A, B, C;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        calcEq();
        this.calcNormaleq();
    }

    public Line(Point2 point1, Point2 point2) {
        this.x1 = point1.x;
        this.x2 = point2.x;
        this.y1 = point1.y;
        this.y2 = point2.y;
        calcEq();
        this.calcNormaleq();
    }

    public Line(Point2 point, Vector2 vector) {
        this.x1 = point.x;
        this.x2 = point.x + vector.getX();
        this.y1 = point.y;
        this.y2 = point.y + vector.getY();
        calcEq();
        this.calcNormaleq();
    }

    void calcEq() {
        k = (y2 - y1) / (x2 - x1);
        b = y1 - k * x1;
    }

    void calcNormaleq() {
        A = y1 - y2;
        B = x2 - x1;
        C = x1 * y2 - x2 * y1;
    }

    public double maxY() {
        return Math.max(y1, y2);
    }

    public double maxX() {
        return Math.max(x1, x2);
    }

    public double minY() {
        return Math.min(y1, y2);
    }

    public double minX() {
        return Math.min(x1, x2);
    }


    public double calcDistance(double x, double y) {
        double d = Math.abs(A * x + B * y + C)
                / Math.sqrt(A * A + B * B);
        return d;
    }

    public boolean doesIntersect(Line line) {
        if (this.A * line.B != this.B * line.A) {
            double ix = -(this.C * line.B - line.C * this.B) / (this.A * line.B - this.B * line.A);
            double iy = -(this.A * line.C - line.A * this.C) / (this.A * line.B - this.B * line.A);
            return (this.minX() <= ix && ix <= this.maxX() &&
                    line.minX() <= ix && ix <= line.maxX() &&
                    this.minY() <= iy && iy <= this.maxY() &&
                    line.minY() <= iy && iy <= line.maxY());
        }
        return false;
    }

    public Point2 findIntPoint(Line line){
        double ix = -(this.C * line.B - line.C * this.B) / (this.A * line.B - this.B * line.A);
        double iy = -(this.A * line.C - line.A * this.C) / (this.A * line.B - this.B * line.A);
        return new Point2(ix, iy);
    }

    @Override
    public String toString() {
        return x1 + "; " + y1 + "  " + x2 + "; " + y2;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(Tools.transformDouble(x1), Tools.transformDouble(y1),
                Tools.transformDouble(x2), Tools.transformDouble(y2));
    }
}
