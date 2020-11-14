package physics.triangle;


import physics.drawing.Drawable;
import physics.geometry.Line;
import physics.geometry.Point2;
import physics.geometry.TPolygon;
import physics.sphere.Material;

import java.awt.*;
import java.util.ArrayList;

public abstract class Triangle implements Drawable {
    public float x0, y0;
    public final float r;
    private Material material;
    public Point2 point1, point2, point3;

    public Triangle(float x0, float y0, float r, Material material) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        setPoints();
        this.material = material;
    }


    public void setPoints() {
        point1 = new Point2((float) (x0 - Math.sqrt(3f) / 2.0f * r), y0 + r / 2.0f);
        point2 = new Point2(x0, y0 - r);
        point3 = new Point2((float) (x0 + Math.sqrt(3) / 2.0f * r), y0 + r / 2.0f);
    }

    public void returnLines(ArrayList<Line> tlines) {
        tlines.add(new Line(point1, point2));
        tlines.add(new Line(point1, point3));
        tlines.add(new Line(point2, point3));

    }

    public int numberOfFixed() {
        return (point1.isFixed() ? 1 : 0) + (point2.isFixed() ? 1 : 0) + (point3.isFixed() ? 1 : 0);
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
