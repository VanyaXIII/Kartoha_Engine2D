package physics.geometry;

public class SphereToLineIntersection {
    public final boolean isIntersected;
    private Point2 collisionPoint;
    private float value;

    public SphereToLineIntersection(boolean isIntersected){this.isIntersected = isIntersected;}
    public SphereToLineIntersection(boolean isIntersected, Point2 collisionPoint, float value){
        this.isIntersected = isIntersected;
        this.value = value;
        this.collisionPoint = collisionPoint;
    }

    public float getValue() {
        return value;
    }

    public Point2 getCollisionPoint() {
        return collisionPoint;
    }
}
