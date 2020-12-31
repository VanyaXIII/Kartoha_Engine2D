package physics.polygons;


import physics.drawing.Drawable;
import physics.geometry.Point2;
import physics.drawing.ArbitraryFigure;
import physics.physics.Material;

import java.awt.*;
import java.util.ArrayList;

public abstract class Polygon implements Drawable {
    public float x0, y0;
    public Material material;
    private ArrayList<Point2> points;

    {
        points = new ArrayList<>();
    }
    public Polygon(float x0, float y0, ArrayList<Point2> points, Material material) {
        this.x0 = x0;
        this.y0 = y0;
        this.points = points;
        this.material = material;
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
        ArbitraryFigure polygon = new ArbitraryFigure(points);
    }


}
