package physics.geometry;

public class PolygonToLineIntersection {

    public final boolean isIntersected;
    private Point2 collisionPoint;
    private Point2 pointOfPolygon;
    private float value;

    public PolygonToLineIntersection(boolean isIntersected) {
        this.isIntersected = isIntersected;
    }

    public PolygonToLineIntersection(boolean isIntersected, Point2 collisionPoint, Point2 pointOfPolygon, float value){
        this.isIntersected = isIntersected;
        this.collisionPoint = collisionPoint;
        this.pointOfPolygon = pointOfPolygon;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public Point2 getPointOfPolygon() {
        return pointOfPolygon;
    }

    public Point2 getCollisionPoint() {
        return collisionPoint;
    }
}
