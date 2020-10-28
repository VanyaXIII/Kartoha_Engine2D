package physics.geometry;

import physics.drawing.Drawable;
import physics.utils.Tools;

import java.awt.*;

public class LineEq implements Drawable {
    public double x1, x2, y1, y2;
    private double k, b;
    private double A, B, C;

    public LineEq(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.calcNormaleq();
        this.calcEq();
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

    public Vector2 makeNormalVector() {
        Vector2 n;
        if (k >= 0) {
            n = new Vector2(minY() - maxY(), maxX() - minX());
        } else n = new Vector2(maxY() - minY(), maxX() - minX());
        return n;
    }

    public double calcDistance(double x, double y) {
        double d = Math.abs(A * x + B * y + C)
                / Math.sqrt(A * A + B * B);
        return d;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(Tools.transformDouble(x1), Tools.transformDouble(y1),
                Tools.transformDouble(x2), Tools.transformDouble(y2));
    }
}
