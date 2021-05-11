package Kartoha_Engine2D.geometry;

import lombok.Getter;

public class PolygonToLineIntersection {

    public final boolean isIntersected;
    @Getter
    private Point2 collisionPoint;
    @Getter
    private Point2 pointOfPolygon;
    @Getter
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

}
