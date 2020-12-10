package physics.geometry;

import physics.drawing.Primitive;
import physics.sphere.ASS;

public class Pair<FirstThing extends Intersectional, SecondThing extends Intersectional> {
    private FirstThing firstThing;
    private SecondThing secondThing;
    private boolean mode;

    public Pair(FirstThing firstThing, SecondThing secondThing, boolean mode) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
        this.mode = mode;
    }


    public boolean isIntersected() {
        if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.WALL)
            return sphereToWall((ASS) firstThing, (Line) secondThing);
        if (firstThing.getType() == Primitive.WALL && secondThing.getType() == Primitive.SPHERE)
            return sphereToWall((ASS) firstThing, (Line) secondThing);
        if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.SPHERE)
            return sphereToSphere((ASS) firstThing, (ASS) secondThing);
        return false;
    }

    private boolean sphereToWall(ASS sphere, Line line) {
        float x = sphere.getCords(mode)[0];
        float y = sphere.getCords(mode)[1];
        float d = line.calcDistance(x, y);
        if (d <= sphere.r) {
            if (line.y1 == line.y2) {
                return x >= line.minX() && x <= line.maxX();
            } else {
                return y >= line.minY() && y <= line.maxY();
            }
        } else
            return line.doesIntersect(new Line(new Point2(sphere.x0, sphere.y0), sphere.v.getMultipliedVector(sphere.getDT())));
    }

    private boolean sphereToSphere(ASS sphere1, ASS sphere2) {
        float x1 = sphere1.getCords(mode)[0];
        float y1 = sphere1.getCords(mode)[1];
        float x2 = sphere2.getCords(mode)[0];
        float y2 = sphere2.getCords(mode)[1];
        Vector2 dvector = new Vector2(x1 - x2,
                y1 - y2);
        float distance = dvector.length();
        if (distance < sphere1.r + sphere2.r) {
            if (sphere2.equals(sphere1)) return false;
            else {
                return true;
            }
        }
        return false;
    }

    public SphereIntersection getSphereIntersection() {
        if (firstThing.getType() != Primitive.SPHERE || secondThing.getType() != Primitive.SPHERE)
            return new SphereIntersection(false);
        ASS sphere1 = (ASS) firstThing;
        ASS sphere2 = (ASS) secondThing;
        float x1 = sphere1.getCords(mode)[0];
        float y1 = sphere1.getCords(mode)[1];
        float x2 = sphere2.getCords(mode)[0];
        float y2 = sphere2.getCords(mode)[1];
        Vector2 dvector = new Vector2(x1 - x2,
                y1 - y2);
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
