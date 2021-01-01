package physics.geometry;

import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class IntersectionalPair<FirstThing extends Collisional, SecondThing extends Collisional> {
    private FirstThing firstThing;
    private SecondThing secondThing;
    private boolean mode;

    public IntersectionalPair(FirstThing firstThing, SecondThing secondThing, boolean mode) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
        this.mode = mode;
    }



    public boolean isIntersected() {
        if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.WALL)
            return sphereToLine((ASS) firstThing, (Line) secondThing);
        if (firstThing.getType() == Primitive.WALL && secondThing.getType() == Primitive.SPHERE)
            return sphereToLine((ASS) secondThing, (Line) firstThing);
        if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.SPHERE)
            return sphereToSphere((ASS) firstThing, (ASS) secondThing);
        if (firstThing.getType() == Primitive.POLYGON && secondThing.getType() == Primitive.SPHERE)
            return sphereToPolygon((ASS) secondThing, (PhysicalPolygon) firstThing);
        if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.POLYGON)
            return sphereToPolygon((ASS) firstThing, (PhysicalPolygon) secondThing);
        return false;
    }

    private boolean sphereToLine(ASS sphere, Line line) {
        if (new AABB(sphere, mode).isIntersectedWith(new AABB(line))) return false;
        Point2 spherePos = sphere.getPosition(mode);
        float d = line.calcDistance(spherePos.x, spherePos.y);
        if (d <= sphere.r) {
            Point2 collisionPoint = line.findIntPoint(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
            return line.isPointOn(collisionPoint);
        } else
            return false;
    }

    private boolean sphereToSphere(ASS sphere1, ASS sphere2) {
        if (new AABB(sphere1, mode).isIntersectedWith(new AABB(sphere2, mode))) return false;
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
        if (new AABB(sphere, mode).isIntersectedWith(new AABB(triangle, mode))) return false;
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
        if (firstThing.getType() != Primitive.SPHERE || secondThing.getType() != Primitive.SPHERE)
            return new SphereIntersection(false);
        if (new AABB((ASS) firstThing, mode).isIntersectedWith(new AABB((ASS) secondThing, mode))) return new SphereIntersection(false);
        ASS sphere1 = (ASS) firstThing;
        ASS sphere2 = (ASS) secondThing;
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

}
