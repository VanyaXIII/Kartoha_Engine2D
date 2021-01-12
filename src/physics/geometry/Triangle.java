package physics.geometry;

public class Triangle {
    private final Point2[] points;

    {
        points = new Point2[3];
    }

    public Triangle(Point2 point1, Point2 point2, Point2 point3) {
        points[0] = point1;
        points[1] = point2;
        points[2] = point3;
    }

    public float getSquare() {
        float side1 = new Vector2(points[0], points[1]).length();
        float side2 = new Vector2(points[1], points[2]).length();
        float side3 = new Vector2(points[2], points[0]).length();
        float p = (side1 + side3 + side2) / 2.0f;

        return (float) Math.sqrt(p * (p - side1) * (p - side2) * (p - side3));
    }

    public Point2 getCentroid() {
        Vector2 rC = new Vector2(new Vector2(points[0]), new Vector2(points[1]));
        rC.add(new Vector2(points[2]));
        rC.mul(1f / 3f);
        return new Point2(rC.getX(), rC.getY());
    }

    public float getJDivDensity(){
        return (getSquare()/36f) *
                (new Vector2(points[0], points[1]).getSquare() +
                new Vector2(points[1], points[2]).getSquare() +
                new Vector2(points[2], points[0]).getSquare());
    }

}
