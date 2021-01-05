package physics.geometry;

import physics.physics.Wall;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class IntersectionalPair<FirstThingType extends Collisional, SecondThingType extends Collisional> {
    private final FirstThingType firstThingType;
    private final SecondThingType secondThingType;
    private final boolean mode;

    public IntersectionalPair(FirstThingType firstThingType, SecondThingType secondThingType, boolean mode) {
        this.firstThingType = firstThingType;
        this.secondThingType = secondThingType;
        this.mode = mode;
    }



    public boolean isIntersected() {
        if (firstThingType instanceof ASS && secondThingType instanceof Wall)
            return sphereToLine((ASS) firstThingType, (Line) secondThingType);
        if (firstThingType instanceof Wall && secondThingType instanceof ASS)
            return sphereToLine((ASS) secondThingType, (Line) firstThingType);
        if (firstThingType instanceof ASS && secondThingType instanceof ASS)
            return sphereToSphere((ASS) firstThingType, (ASS) secondThingType);
        if (firstThingType instanceof PhysicalPolygon && secondThingType instanceof ASS)
            return sphereToPolygon((ASS) secondThingType, (PhysicalPolygon) firstThingType);
        if (firstThingType instanceof ASS && secondThingType instanceof PhysicalPolygon)
            return sphereToPolygon((ASS) firstThingType, (PhysicalPolygon) secondThingType);
        return false;
    }

    private boolean sphereToLine(ASS sphere, Line line) {
        if (!new AABB(sphere, mode).isIntersectedWith(new AABB(line))) return false;
        Point2 spherePos = sphere.getPosition(mode);
        float d = line.calcDistance(spherePos.x, spherePos.y);
        if (d <= sphere.r) {
            Point2 collisionPoint = line.findIntPoint(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
            return line.isPointInBoundingBox(collisionPoint);
        } else
            return false;
    }

    private boolean sphereToSphere(ASS sphere1, ASS sphere2) {
        if (!new AABB(sphere1, mode).isIntersectedWith(new AABB(sphere2, mode))) return false;
        Point2 sphere1Pos = sphere1.getPosition(mode);
        Point2 sphere2Pos = sphere2.getPosition(mode);
        Vector2 dvector = new Vector2(sphere1Pos.x - sphere2Pos.x,
                sphere1Pos.y - sphere2Pos.y);
        if (dvector.getSquare() <= (sphere1.r + sphere2.r) * (sphere1.r + sphere2.r)) {
            return !sphere2.equals(sphere1);
        }
        return false;
    }

    private boolean sphereToPolygon(ASS sphere, PhysicalPolygon triangle){
        if (!new AABB(sphere, mode).isIntersectedWith(new AABB(triangle, mode))) return false;
        ArrayList<Line> lines = triangle.getLines(mode);
        boolean intersected = false;
        for(Line line : lines){
            if (sphereToLine(sphere, line)){
                intersected = true;
                break;
            }
        }
        return intersected;
    }

    public SphereIntersection getSphereIntersection() {
        if (!(firstThingType instanceof ASS || secondThingType instanceof ASS))
            return new SphereIntersection(false);
        if (!new AABB((ASS) firstThingType, mode).isIntersectedWith(new AABB((ASS) secondThingType, mode))) return new SphereIntersection(false);
        ASS sphere1 = (ASS) firstThingType;
        ASS sphere2 = (ASS) secondThingType;
        Point2 sphere1Pos = sphere1.getPosition(mode);
        Point2 sphere2Pos = sphere2.getPosition(mode);
        Vector2 dvector = new Vector2(sphere1Pos.x - sphere2Pos.x,
                sphere1Pos.y - sphere2Pos.y);
        float distance = dvector.length();
        if (distance < sphere1.r + sphere2.r) {
            if (sphere2.equals(sphere1)) {
                return new SphereIntersection(false);
            } else {
                if (distance != 0)
                    return new SphereIntersection(true, dvector, sphere2.r + sphere1.r - distance);
                else
                    return new SphereIntersection(true, dvector, 0);
            }
        }
        return new SphereIntersection(false);
    }

    public SphereToLineIntersection getSphereToLineIntersection(){
        if (!(firstThingType instanceof ASS || secondThingType instanceof Wall))
            return new SphereToLineIntersection(false);
        if (!new AABB((ASS) firstThingType, mode).isIntersectedWith(new AABB((Wall) secondThingType))) return new SphereToLineIntersection(false);
        if (sphereToLine((ASS) firstThingType, (Line) secondThingType)){
            ASS sphere = (ASS) firstThingType;
            Line line = (Line) secondThingType;
            Point2 spherePos = sphere.getPosition(mode);
            float d = line.calcDistance(spherePos.x, spherePos.y);
            Point2 collisionPoint = line.findIntPoint(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
            return new SphereToLineIntersection(true, collisionPoint, sphere.r - d);
        }
        return new SphereToLineIntersection(false);
    }

}
