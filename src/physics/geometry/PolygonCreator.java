package physics.geometry;

import java.util.ArrayList;

public class PolygonCreator {
    private final int numOfPoints;
    private final float r;
    private final Point2 centrePoint;

    public PolygonCreator(Point2 centrePoint, int numOfPoints, float r) {
        this.centrePoint = centrePoint;
        this.numOfPoints = numOfPoints;
        this.r = r;
    }

    public ArrayList<Point2> createPolygonPoints() {
        Vector2 radVector = new Vector2((float) Math.random(), (float) Math.random());
        radVector.setLength(r);
        ArrayList<Point2> points = new ArrayList<>();
        float[] angles = new float[numOfPoints];
        float sumOfAngles = 0f;
        for (int i = 0; i < numOfPoints; i++) {
            angles[i] = (float) Math.random();
            sumOfAngles += angles[i];
        }
        for (int i = 0; i < numOfPoints; i++) {
            angles[i] /= sumOfAngles;
        }

        for (int i = 0; i < numOfPoints; i++) {
            points.add(radVector.movePoint(centrePoint, r));
            if (i != numOfPoints - 1) radVector.rotate(2 * (float) Math.PI * angles[i]);
        }
        return points;
    }

}
