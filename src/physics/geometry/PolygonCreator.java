package physics.geometry;

import physics.polygons.PhysicalPolygon;
import physics.polygons.Polygon;

import java.util.ArrayList;

public class PolygonCreator {
    private int numOfPoints;
    private float maxRadius;
    private Point2 centrePoint;

    public PolygonCreator(Point2 centrePoint, int numOfPoints, float maxRadius) {
        this.centrePoint = centrePoint;
        this.numOfPoints = numOfPoints;
        this.maxRadius = maxRadius;
    }

    public ArrayList<Point2> createPolygonPoints() {
        float angle = (float) (2f * Math.PI / numOfPoints);
        Vector2 radVector = new Vector2((float) Math.random(), (float) Math.random());
        radVector.setLength(maxRadius);
        ArrayList<Point2> points = new ArrayList<>();
        for (int i = 0; i < numOfPoints; i++){
            float movement = (float) ((Math.random()) * maxRadius);
            points.add(radVector.movePoint(centrePoint, movement));
            radVector.rotate(angle);
        }
        return points;
    }
}
