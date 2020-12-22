package physics.triangle;


import physics.drawing.Drawable;
import physics.geometry.Line;
import physics.geometry.Point2;
import physics.geometry.TPolygon;
import physics.physics.Material;

import java.awt.*;
import java.util.ArrayList;

public abstract class Triangle implements Drawable {
    public float x0, y0;
    public final float r;
    public Material material;
    private ArrayList<Point2> points;

    {
        points = new ArrayList<>();
    }
    public Triangle(float x0, float y0, float r, Material material) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        setPoints();
        this.material = material;
    }


    public void setPoints() {
        points.add(new Point2((float) (x0 - Math.sqrt(3f) / 2.0f * r), y0 + r / 2.0f));
        points.add(new Point2(x0, y0 - r));
        points.add(new Point2((float) (x0 + Math.sqrt(3) / 2.0f * r), y0 + r / 2.0f));
    }


    public ArrayList<Point2> getPoints() {
        return points;
    }

    public Point2 getPoint(int index){
        return points.get(index);
    }

    public ArrayList<Point2> clonePoints(){
        ArrayList<Point2> newPoints = new ArrayList<>();
        for(Point2 point : points) newPoints.add(new Point2(point.x, point.y));
        return newPoints;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        TPolygon polygon = new TPolygon(points.get(0), points.get(1), points.get(2));
        g.fillPolygon(polygon);
    }


}
