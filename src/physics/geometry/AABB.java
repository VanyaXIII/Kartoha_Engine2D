package physics.geometry;

import physics.sphere.ASS;
import physics.triangle.AST;

public class AABB {
    public final Point2 min;
    public final Point2 max;

    public AABB(Point2 point1, Point2 point2) {
        if (point2.x > point1.x) {
            this.min = point1;
            this.max = point2;
        }else {
            this.min = point2;
            this.max = point1;
        }
    }

    public AABB(Line line){
        min = new Point2(line.minX(), line.minY());
        max = new Point2(line.maxX(), line.maxY());
    }

    public AABB(ASS sphere, boolean mode){
        Point2 position = sphere.getPosition(mode);
        min = new Point2(position.x - sphere.r, position.y - sphere.r);
        max = new Point2(position.x + sphere.r, position.y + sphere.r);
    }

    public AABB(AST triangle, boolean mode){
        Point2 position = triangle.getPosition(mode);
        min = new Point2(position.x - triangle.r, position.y - triangle.r);
        max = new Point2(position.x + triangle.r, position.y + triangle.r);
    }

    public boolean isIntersectedWith(AABB b){
        if (this.max.x < b.min.x || this.min.x > b.max.x) return false;
        if (this.max.y < b.min.y || this.min.y > b.max.y) return false;
        return true;
    }
}
