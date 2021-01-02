package physics.geometry;

import physics.sphere.ASS;

public class SphereToLineIntersection {
    public final boolean isIntersected;
    private Point2 collisionalPoint;
    private float value;

    public SphereToLineIntersection(boolean isIntersected){this.isIntersected = isIntersected;}
    public SphereToLineIntersection(boolean isIntersected, Point2 collisionalPoint, float value){
        this.isIntersected = isIntersected;
        this.value = value;
        this.collisionalPoint = collisionalPoint;
    }

    public float getValue() {
        return value;
    }

    public Point2 getCollisionalPoint() {
        return collisionalPoint;
    }
}
