package physics.geometry;

import physics.limiters.Intersectional;
import physics.physics.Intersecter;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;
import physics.utils.TripleMap;

import java.util.ArrayList;
import java.util.Objects;

public class IntersectionalPair<FirstThingType extends Intersectional, SecondThingType extends Intersectional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static boolean dynamicCollisionMode = true;
    private final static boolean staticCollisionMode = false;
    private final static TripleMap<Class, Class, Intersecter> methodsMap;

    public IntersectionalPair(FirstThingType firstThing, SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }

    static {
        methodsMap = new TripleMap<>();

        methodsMap.addFirstKey(ASS.class);
        methodsMap.addFirstKey(Line.class);
        methodsMap.addFirstKey(PhysicalPolygon.class);

        methodsMap.putByFirstKey(ASS.class, Line.class, IntersectionalPair::sphereToLine);
        methodsMap.putByFirstKey(ASS.class, ASS.class, IntersectionalPair::sphereToSphere);
        methodsMap.putByFirstKey(ASS.class, PhysicalPolygon.class, IntersectionalPair::sphereToPolygon);

        methodsMap.putByFirstKey(Line.class, ASS.class, IntersectionalPair::sphereToLine);
        methodsMap.putByFirstKey(Line.class, PhysicalPolygon.class, IntersectionalPair::polygonToWall);

        methodsMap.putByFirstKey(PhysicalPolygon.class, Line.class, IntersectionalPair::polygonToWall);
        methodsMap.putByFirstKey(PhysicalPolygon.class, ASS.class, IntersectionalPair::sphereToPolygon);
    }

    public boolean isIntersected() {
        return methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).isIntersected(firstThing, secondThing);
    }

    private static boolean sphereToLine(Intersectional thing1, Intersectional thing2) {

        ASS sphere;
        Line line;

        if (thing1 instanceof ASS){
            sphere = (ASS) thing1;
            line = (Line) thing2;
        }
        else {
            sphere = (ASS) thing2;
            line = (Line) thing1;
        }

        if (!new AABB(sphere, dynamicCollisionMode).isIntersectedWith(new AABB(Objects.requireNonNull(line))))
            return false;

        Point2 spherePos = sphere.getPosition(dynamicCollisionMode);
        float d = line.calcDistance(spherePos.x, spherePos.y);

        if (d <= sphere.getR()) {
            Point2 collisionPoint = line.findIntPointWith(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
            if (line.isPointInBoundingBox(collisionPoint)) return true;
            else return new Vector2(spherePos, new Point2(line.x1, line.y1)).length() <= sphere.getR()
                    || new Vector2(spherePos, new Point2(line.x2, line.y2)).length() <= sphere.getR();
        }

        return false;
    }

    private static boolean sphereToSphere(Intersectional thing1, Intersectional thing2) {
        ASS sphere1 = (ASS) thing1;
        ASS sphere2 = (ASS) thing2;

        if (!new AABB(sphere1, dynamicCollisionMode).isIntersectedWith(new AABB(sphere2, dynamicCollisionMode)))
            return false;

        Point2 sphere1Pos = sphere1.getPosition(dynamicCollisionMode);
        Point2 sphere2Pos = sphere2.getPosition(dynamicCollisionMode);
        Vector2 distanceVector = new Vector2(sphere1Pos.x - sphere2Pos.x,
                sphere1Pos.y - sphere2Pos.y);
        if (distanceVector.getSquare() <= (sphere1.getR() + sphere2.getR()) * (sphere1.getR() + sphere2.getR())) {
            return !sphere2.equals(sphere1);
        }

        return false;
    }

    private static boolean sphereToPolygon(Intersectional thing1, Intersectional thing2) {
        PhysicalPolygon polygon;
        ASS sphere;

        if (thing1 instanceof PhysicalPolygon){
            polygon = (PhysicalPolygon) thing1;
            sphere = (ASS) thing2;
        }
        else{
            polygon = (PhysicalPolygon) thing2;
            sphere = (ASS) thing1;
        }

        if (!new AABB(sphere, dynamicCollisionMode).isIntersectedWith(new AABB(polygon, dynamicCollisionMode)))
            return false;

        ArrayList<Line> lines = polygon.getLines(dynamicCollisionMode);
        for (Line line : lines) {
            if (sphereToLine(sphere, line)) {
                return true;
            }
        }

        return false;
    }

    private static boolean polygonToWall(Intersectional thing1, Intersectional thing2) {
        PhysicalPolygon polygon;
        Line line;

        if (thing1 instanceof PhysicalPolygon) {
            polygon = (PhysicalPolygon) thing1;
            line = (Line) thing2;
        }
        else {
            polygon = (PhysicalPolygon) thing2;
            line = (Line) thing1;
        }

        if (!new AABB(polygon, dynamicCollisionMode).isIntersectedWith(new AABB(line)))
            return false;

        byte counter = 0;
        for (Line polygonLine : polygon.getLines(dynamicCollisionMode)) {
            if (line.doesIntersectBySegmentsWith(polygonLine)) counter++;
        }

        return counter > 0;
    }

    public SpheresIntersection getSpheresIntersection() {
        if (!(firstThing instanceof ASS && secondThing instanceof ASS))
            return new SpheresIntersection(false);

        if (!new AABB((ASS) firstThing, staticCollisionMode).isIntersectedWith(new AABB((ASS) secondThing, staticCollisionMode)))
            return new SpheresIntersection(false);

        ASS sphere1 = (ASS) firstThing;
        ASS sphere2 = (ASS) secondThing;
        Point2 sphere1Pos = sphere1.getPosition(staticCollisionMode);
        Point2 sphere2Pos = sphere2.getPosition(staticCollisionMode);
        Vector2 distanceVector = new Vector2(sphere1Pos.x - sphere2Pos.x,
                sphere1Pos.y - sphere2Pos.y);
        float distance = distanceVector.length();

        if (distance < sphere1.getR() + sphere2.getR()) {
            if (sphere2.equals(sphere1)) {
                return new SpheresIntersection(false);
            } else {
                if (distance != 0)
                    return new SpheresIntersection(true, distanceVector, sphere2.getR() + sphere1.getR() - distance);
                else
                    return new SpheresIntersection(true, distanceVector, 0);
            }
        }

        return new SpheresIntersection(false);
    }

    public SphereToLineIntersection getSphereToLineIntersection() {
        if (!(firstThing instanceof ASS && secondThing instanceof Line))
            return new SphereToLineIntersection(false);

        if (!new AABB((ASS) firstThing, staticCollisionMode).isIntersectedWith(new AABB((Line) secondThing)))
            return new SphereToLineIntersection(false);

        ASS sphere = (ASS) firstThing;
        Line line = (Line) secondThing;
        Point2 spherePos = sphere.getPosition(staticCollisionMode);
        float d = line.calcDistance(spherePos.x, spherePos.y);

        if (d <= sphere.getR()) {
            Point2 collisionPoint = line.findIntPointWith(new Line(new Point2(spherePos.x, spherePos.y), new Vector2(line).createNormal()));
            if (line.isPointInBoundingBox(collisionPoint)) {
                return new SphereToLineIntersection(true, collisionPoint, sphere.getR() - d);
            }

            else if (new Vector2(spherePos, new Point2(line.x1, line.y1)).length() <= sphere.getR())
                return new SphereToLineIntersection(true, new Point2(line.x1, line.y1), sphere.getR() -
                        new Vector2(spherePos, new Point2(line.x1, line.y1)).length());

            else if (new Vector2(spherePos, new Point2(line.x2, line.y2)).length() <= sphere.getR())
                return new SphereToLineIntersection(true, new Point2(line.x2, line.y2), sphere.getR() -
                        new Vector2(spherePos, new Point2(line.x2, line.y2)).length());
        }

        return new SphereToLineIntersection(false);

    }

    public PolygonToLineIntersection getPolygonToLineIntersection() {
        if (!(firstThing instanceof PhysicalPolygon && secondThing instanceof Line))
            return new PolygonToLineIntersection(false);

        if (!new AABB((PhysicalPolygon) firstThing, staticCollisionMode).isIntersectedWith(new AABB((Line) secondThing)))
            return new PolygonToLineIntersection(false);

        PhysicalPolygon polygon = (PhysicalPolygon) firstThing;
        Line line = (Line) secondThing;
        Point2 position = polygon.getPositionOfCentre(staticCollisionMode);
        final Point2[] farPoint = {null};

        polygon.getPoints(staticCollisionMode).forEach(point -> {
            if (farPoint[0] == null && new Line(point, position).doesIntersectBySegmentsWith(line)) farPoint[0] = point;
            if (new Line(point, position).doesIntersectBySegmentsWith(line) &&
                    line.calcDistance(point) > line.calcDistance(farPoint[0])) {
                farPoint[0] = point;
            }

        });

        if (farPoint[0] != null) {
            Point2 collisionPoint = line.findIntPointWith(new Line(new Point2(farPoint[0].x, farPoint[0].y), new Vector2(line).createNormal()));
            return new PolygonToLineIntersection(true, collisionPoint, farPoint[0], line.calcDistance(farPoint[0]));
        }

        return new PolygonToLineIntersection(false);

    }

    public static boolean getStaticCollisionMode(){
        return staticCollisionMode;
    }
    public static boolean getDynamicCollisionMode(){
        return dynamicCollisionMode;
    }

}
