package physics.geometry;

import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class AABB {
    public final Point2 min;
    public final Point2 max;

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

    public AABB(ASS sphere, boolean mode) {
        Point2 position = sphere.getPosition(mode);
        min = new Point2(position.x - sphere.getR(), position.y - sphere.getR());
        max = new Point2(position.x + sphere.getR(), position.y + sphere.getR());
    }

    public AABB(PhysicalPolygon polygon, boolean mode) {
        Point2 position = polygon.getPositionOfCentre(mode);
        ArrayList<Point2> points = polygon.getPoints(mode);
        float posXDeviation = 0f;
        float posYDeviation = 0f;
        float negXDeviation = points.get(0).x;
        float negYDeviation = points.get(0).y;

        for (Point2 point : points) {
            Vector2 vectorToPoint = new Vector2(point, position);
            if (posXDeviation < vectorToPoint.getX()) posXDeviation = vectorToPoint.getX();
            if (posYDeviation < vectorToPoint.getY()) posYDeviation = vectorToPoint.getY();
            if (negXDeviation > vectorToPoint.getX()) negXDeviation = vectorToPoint.getX();
            if (negYDeviation > vectorToPoint.getY()) negYDeviation = vectorToPoint.getY();
        }

        min = new Point2(position.x + negXDeviation, position.y + negXDeviation);
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
}
