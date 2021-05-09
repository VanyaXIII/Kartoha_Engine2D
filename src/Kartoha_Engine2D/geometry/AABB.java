package Kartoha_Engine2D.geometry;

import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.polygons.PhysicalPolygon;

import java.awt.*;
import java.util.ArrayList;

public class AABB {

    private final Point2 min;
    private final Point2 max;

    public AABB(Point2 point1, Point2 point2) {
        if (point2.x > point1.x) {
            this.min = point1;
            this.max = point2;
        } else {
            this.min = point2;
            this.max = point1;
        }
    }

    public AABB(Line line) {
        min = new Point2(line.minX(), line.minY());
        max = new Point2(line.maxX(), line.maxY());
    }

    public AABB(PhysicalSphere sphere, boolean mode) {
        Point2 position = sphere.getPositionOfCentre(mode);
        min = new Point2(position.x - sphere.getR(), position.y - sphere.getR());
        max = new Point2(position.x + sphere.getR(), position.y + sphere.getR());
    }

    public AABB(PhysicalPolygon polygon, boolean mode) {
        Point2 position = polygon.getPositionOfCentre(mode);
        ArrayList<Point2> points = polygon.getPoints(mode);
        float posXDeviation = 0f;
        float posYDeviation = 0f;
        float negXDeviation = 0f;
        float negYDeviation = 0f;

        for (Point2 point : points) {
            Vector2 vectorToPoint = new Vector2(position, point);
            if (posXDeviation < vectorToPoint.getX()) posXDeviation = vectorToPoint.getX();
            if (posYDeviation < vectorToPoint.getY()) posYDeviation = vectorToPoint.getY();
            if (negXDeviation > vectorToPoint.getX()) negXDeviation = vectorToPoint.getX();
            if (negYDeviation > vectorToPoint.getY()) negYDeviation = vectorToPoint.getY();
        }

        min = new Point2(position.x + negXDeviation, position.y + negYDeviation);
        max = new Point2(position.x + posXDeviation, position.y + posYDeviation);
    }


    public boolean isIntersectedWith(AABB b) {
        if (this.max.x < b.min.x || this.min.x > b.max.x) return false;
        if (this.max.y < b.min.y || this.min.y > b.max.y) return false;
        return true;
    }

    public boolean doesContainPoint(Point2 point) {
        return point.x >= min.x && point.x <= max.x
                && point.y >= min.y && point.y <= max.y;
    }

    public Point2 getMin() {
        return min;
    }

    public Point2 getMax() {
        return max;
    }

    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.drawRect((int)min.x, (int)min.y, (int)(max.x - min.x), (int) (max.y - min.y));
    }
}
