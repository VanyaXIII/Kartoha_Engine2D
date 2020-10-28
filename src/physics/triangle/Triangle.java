package physics.triangle;


import physics.drawing.Drawable;
import physics.geometry.LineEq;
import physics.geometry.Point2D;
import physics.sphere.Material;
import physics.utils.Tools;

import javax.tools.Tool;
import java.awt.*;
import java.util.ArrayList;

public class Triangle implements Drawable {
    public double x0, y0;
    public final double r;
    private Material material;

    public Triangle(double x0, double y0, double r) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
    }
    //TODO подумать над отрисовкой и столконовением шариков
    @Override
    public void draw(Graphics g) {
        Polygon polygon = new Polygon(new int[]{
                Tools.transformDouble(x0 - Math.sqrt(3) / 2.0 * r),
                Tools.transformDouble(x0),
                Tools.transformDouble(x0 + Math.sqrt(3) / 2.0 * r)},
                new int[]{
                Tools.transformDouble(y0 - r),
                Tools.transformDouble(y0 + r / 2.0),
                Tools.transformDouble(y0 + r / 2.0)},
                3);
        g.drawPolygon(polygon);
    }


}
