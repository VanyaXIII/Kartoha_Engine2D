package physics.geometry;

import physics.physics.Wall;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class IntersectionalPair<FirstThingType extends Collisional, SecondThingType extends Collisional> {
    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final boolean mode;

    public IntersectionalPair(FirstThingType firstThing, SecondThingType secondThing, boolean mode) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
        this.mode = mode;
    }


    public boolean isIntersected() {
        if (firstThing instanceof ASS && secondThing instanceof Wall)
            return sphereToLine((ASS) firstThing, (Line) secondThing);
        else if (firstThing instanceof Wall && secondThing instanceof ASS)
            return sphereToLine((ASS) secondThing, (Line) firstThing);
        else if (firstThing instanceof ASS && secondThing instanceof ASS)
            return sphereToSphere((ASS) firstThing, (ASS) secondThing);
        else if (firstThing instanceof PhysicalPolygon && secondThing instanceof ASS)
            return sphereToPolygon((ASS) secondThing, (PhysicalPolygon) firstThing);
        else if (firstThing instanceof ASS && secondThing instanceof PhysicalPolygon)
            return sphereToPolygon((ASS) firstThing, (PhysicalPolygon) secondThing);
        else if (firstThing instanceof PhysicalPolygon && secondThing instanceof Wall)
            return polygonToWall((PhysicalPolygon) firstThing, (Line) secondThing);
        else if (firstThing instanceof Wall && secondThing instanceof PhysicalPolygon)
            return polygonToWall((PhysicalPolygon) secondThing, (Line) firstThing);
        return false;
    }

    private boolean sphereToLine(ASS sphere, Line line) {
        if (!new AABB(sphere, mode).isIntersectedWith(new AABB(line))) return false;
        Point2 spherePos = sphere.getPosition(mode);
        float d = line.calcDistance(spherePos.x, spherePos.y);
        if (d <= sphere.r) {
            Point2 collisionPoint = line.findIntPointWith(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
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

    private boolean sphereToPolygon(ASS sphere, PhysicalPolygon triangle) {
        if (!new AABB(sphere, mode).isIntersectedWith(new AABB(triangle, mode))) return false;
        ArrayList<Line> lines = triangle.getLines(mode);
        boolean intersected = false;
        for (Line line : lines) {
            if (sphereToLine(sphere, line)) {
                intersected = true;
                break;
            }
        }
        return intersected;
    }

    private boolean polygonToWall(PhysicalPolygon polygon, Line line) {
        if (!new AABB(polygon, mode).isIntersectedWith(new AABB(line))) return false;
        byte counter = 0;
        for (Line polygonLine : polygon.getLines(mode)) {
            if (line.doesIntersectBySegmentsWith(polygonLine)) counter++;
        }
        return counter == 2;
    }

    public SphereIntersection getSphereIntersection() {
        if (!(firstThing instanceof ASS || secondThing instanceof ASS))
            return new SphereIntersection(false);
        if (!new AABB((ASS) firstThing, mode).isIntersectedWith(new AABB((ASS) secondThing, mode)))
            return new SphereIntersection(false);
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

    public SphereToLineIntersection getSphereToLineIntersection() {
        if (!(firstThing instanceof ASS || secondThing instanceof Wall))
            return new SphereToLineIntersection(false);
        if (!new AABB((ASS) firstThing, mode).isIntersectedWith(new AABB((Wall) secondThing)))
            return new SphereToLineIntersection(false);
        if (sphereToLine((ASS) firstThing, (Line) secondThing)) {
            ASS sphere = (ASS) firstThing;
            Line line = (Line) secondThing;
            Point2 spherePos = sphere.getPosition(mode);
            float d = line.calcDistance(spherePos.x, spherePos.y);
            Point2 collisionPoint = line.findIntPointWith(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
            return new SphereToLineIntersection(true, collisionPoint, sphere.r - d);
        }
        return new SphereToLineIntersection(false);
    }

}
