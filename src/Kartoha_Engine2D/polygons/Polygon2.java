package Kartoha_Engine2D.polygons;


import Kartoha_Engine2D.drawing.Drawable;
import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.drawing.ArbitraryFigure;
import Kartoha_Engine2D.geometry.Vector2;
import Kartoha_Engine2D.physics.Material;

import java.awt.*;
import java.util.ArrayList;

public abstract class Polygon2 implements Drawable {
    public float x0, y0;
    private ArrayList<Point2> points;

    {
        points = new ArrayList<>();
    }

    public Polygon2(float x0, float y0, ArrayList<Point2> points) {
        this.x0 = x0;
        this.y0 = y0;
        this.points = points;
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

    public void move(Vector2 movementVector, float movement){
        Point2 nCoords = movementVector.movePoint(new Point2(x0, y0), movement);
        x0 = nCoords.x;
        y0 = nCoords.y;
        points.forEach(point -> {
            Point2 nPoint = movementVector.movePoint(point, movement);
            point.x = nPoint.x;
            point.y = nPoint.y;
        });
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        ArbitraryFigure polygon = new ArbitraryFigure(points);
        g.drawPolygon(polygon.getPolygon());
    }


}
