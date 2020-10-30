package physics.triangle;


import physics.drawing.Drawable;
import physics.geometry.LineEq;
import physics.geometry.Point2;
import physics.geometry.TPolygon;
import physics.sphere.Material;
import physics.utils.Tools;

import java.awt.*;
import java.util.ArrayList;

public abstract class Triangle implements Drawable {
    public double x0, y0;
    public final double r;
    private Material material;
    public Point2 point1, point2, point3;

    public Triangle(double x0, double y0, double r, Material material) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        setPoints();
        this.material = material;
    }

    public Triangle(double x0, double y0, double r) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        setPoints();
        this.material = new Material(1000, Color.RED);
    }

    public void setPoints() {
        point1 = new Point2(x0 - Math.sqrt(3) / 2.0 * r, y0 + r / 2.0);
        point2 = new Point2(x0, y0 - r);
        point3 = new Point2(x0 + Math.sqrt(3) / 2.0 * r, y0 + r / 2.0);
    }

    public void returnLines(ArrayList<LineEq> tlines){
        tlines.add(new LineEq(point1, point2));
        tlines.add(new LineEq(point1, point3));
        tlines.add(new LineEq(point2, point3));

    }


    public Color getColor() {
        return material.color;
    }
    /*TODO подумать над отрисовкой и столконовением шариков
        треугольник как массив линий, продумать измененеие всех сразу */

    @Override
    public void draw(Graphics g) {
        g.setColor(material.color);
        TPolygon polygon = new TPolygon(point1, point2, point3);
        g.fillPolygon(polygon);
    }


}
