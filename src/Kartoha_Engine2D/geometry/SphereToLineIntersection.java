package Kartoha_Engine2D.geometry;

import lombok.Getter;

public class SphereToLineIntersection {

    public final boolean isIntersected;
    @Getter
    private Point2 collisionPoint;
    @Getter
    private float value;

    public SphereToLineIntersection(boolean isIntersected){this.isIntersected = isIntersected;}

    public SphereToLineIntersection(boolean isIntersected, Point2 collisionPoint, float value){
        this.isIntersected = isIntersected;
        this.value = value;
        this.collisionPoint = collisionPoint;
    }

}
