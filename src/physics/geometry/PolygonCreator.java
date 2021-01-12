package physics.geometry;

import java.util.ArrayList;

public class PolygonCreator {
    ;
    private ArrayList<Point2> points;
    private ArrayList<Triangle> triangles;

    {
        points = new ArrayList<>();
        triangles = new ArrayList<>();
    }

    public PolygonCreator(Point2 centrePoint, int numOfPoints, float r) {
        setPoints(centrePoint, r, numOfPoints);
        triangulate();
    }

    public PolygonCreator(ArrayList<Point2> points) {
        this.points = points;
        triangulate();
    }

    private void setPoints(Point2 centrePoint, float r, int numOfPoints) {
        Vector2 radVector = new Vector2((float) Math.random(), (float) Math.random());
        radVector.setLength(r);
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
    }

    public ArrayList<Point2> getPoints() {
        return points;
    }

    public void triangulate() {
        for (int i = 2; i < points.size(); i++) {
            triangles.add(new Triangle(points.get(0), points.get(i - 1), points.get(i)));
        }
    }


    public Point2 getCentreOfMass() {
        Vector2 rC = new Vector2(0, 0);
        float s = 0f;
        for (Triangle triangle : triangles) {
            Vector2 rCOfTriangle = new Vector2(triangle.getCentroid());
            rCOfTriangle.mul(triangle.getSquare());
            s += triangle.getSquare();
            rC.add(rCOfTriangle);

        }
        rC.mul(1f / s);
        return new Point2(rC.getX(), rC.getY());
    }

    public float getSquare() {
        float square = 0f;
        for (Triangle triangle : triangles) {
            square += triangle.getSquare();
        }
        return square;
    }

    public float getJDivDensity() {
        Point2 c = getCentreOfMass();
        float J = 0f;
        for (Triangle triangle : triangles) {
            J += triangle.getJDivDensity() + triangle.getSquare() * new Vector2(c, triangle.getCentroid()).getSquare();
        }
        return J;
    }
}
