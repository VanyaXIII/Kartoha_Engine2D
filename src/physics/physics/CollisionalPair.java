package physics.physics;


import physics.geometry.*;
import physics.limiters.Collisional;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;
import physics.utils.FloatComparator;
import physics.utils.Tools;
import physics.utils.TripleMap;

import java.util.ArrayList;

public class CollisionalPair<FirstThingType extends Collisional, SecondThingType extends Collisional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static TripleMap<Class, Class, Collider> methodsMap;

    public CollisionalPair(FirstThingType firstThing, SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }


    static {
        methodsMap = new TripleMap<>();
        methodsMap.addFirstKey(ASS.class);
        methodsMap.addFirstKey(Wall.class);
        methodsMap.addFirstKey(PhysicalPolygon.class);
        methodsMap.putByFirstKey(ASS.class, Wall.class, CollisionalPair::sphereToWall);
        methodsMap.putByFirstKey(ASS.class, ASS.class, CollisionalPair::sphereToSphere);
        methodsMap.putByFirstKey(ASS.class, PhysicalPolygon.class, CollisionalPair::sphereToPolygon);
        methodsMap.putByFirstKey(Wall.class, ASS.class, CollisionalPair::sphereToWall);
        methodsMap.putByFirstKey(Wall.class, PhysicalPolygon.class, CollisionalPair::polygonToWall);
        methodsMap.putByFirstKey(PhysicalPolygon.class, Wall.class, CollisionalPair::polygonToWall);
        methodsMap.putByFirstKey(PhysicalPolygon.class, ASS.class, CollisionalPair::sphereToPolygon);

    }


    public void collide() {
        methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).collide(firstThing, secondThing);
    }

    private static void sphereToWall(Collisional thing1, Collisional thing2) {
        ASS sphere;
        Wall wall;
        if (thing1 instanceof ASS) {
            sphere = (ASS) thing1;
            wall = (Wall) thing2;
        } else {
            wall = (Wall) thing1;
            sphere = (ASS) thing2;
        }
        final Vector2 axisX = new Vector2(wall);
        final Vector2 axisY = axisX.createNormal();
        final float fr = Tools.countAverage(sphere.getMaterial().coefOfFriction, wall.getMaterial().coefOfFriction);
        final float k = Tools.countAverage(sphere.getMaterial().coefOfReduction, wall.getMaterial().coefOfReduction);
        if (sphere.getV().countProjectionOn(axisY) > 0f) axisY.makeOp();
        Vector2 radVector = axisY.createByFloat(-sphere.getR());
        final float w1x = radVector.getCrossProduct(sphere.getW()).countProjectionOn(axisX) / sphere.getR();
        final float v1x = sphere.getV().countProjectionOn(axisX);
        final float v1y = sphere.getV().countProjectionOn(axisY);
        float v2x = v1x;
        float w2x = w1x;
        boolean slips = true;
        float sDivM = fr * (1 + k) * Math.abs(v1y);
        final float r = sphere.getR();
        if ((Math.abs(0.5f * v1x + 0.5f * w1x * r) / (Math.abs(v1y) * (1 + k) * 1.5f)) < fr) slips = false;
        if (slips && !FloatComparator.equals(w1x * r + v1x, 0f)) {
            v2x = v1x - Tools.sign(w1x * r + v1x) * sDivM;
            w2x = w1x - 2 * Tools.sign(w1x * r + v1x) * sDivM / r;
        } else if (!FloatComparator.equals(w1x * r + v1x, 0f)) {
            v2x = (-0.5f * w1x * r + v1x) / 1.5f;
            w2x = -v2x / r;
        }
        sphere.setW(Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * r), radVector));
        Vector2 fv1x = axisX.createByFloat(v2x);
        Vector2 fv1y = axisY.createByFloat(-v1y * k);
        sphere.setV(new Vector2(fv1x, fv1y));
    }

    private static void sphereToSphere(Collisional thing1, Collisional thing2) {
        ASS sphere1 = (ASS) thing1;
        ASS sphere2 = (ASS) thing2;
        final Point2 firstSpherePos = sphere1.getPosition(false);
        final Point2 secondSpherePos = sphere2.getPosition(false);
        final Vector2 axisX = new Vector2(firstSpherePos.x - secondSpherePos.x,
                firstSpherePos.y - secondSpherePos.y);
        if (axisX.length() != 0.0f) {
            final float m1 = sphere1.getM();
            final float m2 = sphere2.getM();
            final float ratio = m1 / m2;
            Vector2 axisY = axisX.createNormal();
            final float k = Tools.countAverage(sphere1.getMaterial().coefOfReduction, sphere2.getMaterial().coefOfReduction);
            final float fr = Tools.countAverage(sphere1.getMaterial().coefOfFriction, sphere2.getMaterial().coefOfFriction);
            final float v1x = sphere1.getV().countProjectionOn(axisX);
            final float v2x = sphere2.getV().countProjectionOn(axisX);
            final float v1y = sphere1.getV().countProjectionOn(axisY);
            final float v2y = sphere2.getV().countProjectionOn(axisY);
            final float s = (m1 * m2) / (m1 + m2) * (1f + k) * Math.abs(v1x - v2x);
            final float r1 = sphere1.getR();
            final float r2 = sphere2.getR();
            Vector2 radVector1 = axisX.createByFloat(-r1);
            final float w1y = radVector1.getCrossProduct(sphere1.getW()).countProjectionOn(axisY) / r1;
            Vector2 radVector2 = axisX.createByFloat(+r2);
            float w2y = radVector2.getCrossProduct(sphere2.getW()).countProjectionOn(axisY) / r2;
            boolean slips = true;
            if (Math.abs((v2y + w2y * r2) - (v1y + w1y * r1)) / (3f * s * Math.abs(1f / m1 + 1f / m2)) < fr)
                slips = false;
            float u1y = v1y, u2y = v2y;
            float fw1y = w1y, fw2y = w2y;
            if (Math.signum(v1y + w1y * r1) == Math.signum(v2y + w2y * r2) && slips && !FloatComparator.equals(v2y + w2y * r2, v1y + w1y * r1)) {
                float sign = Math.signum(Math.abs(v1y + w1y * r1) - Math.abs(v2y + w2y * r2));
                u1y = v1y - sign * Tools.sign(v1y + w1y * r1) * fr * s / m1;
                u2y = v2y + sign * Tools.sign(v2y + w2y * r2) * fr * s / m2;
                fw1y = w1y - sign * Tools.sign(v1y + w1y * r1) * 2f * fr * s / (m1 * r1);
                fw2y = w2y + sign * Tools.sign(v2y + w2y * r2) * 2f * fr * s / (m2 * r2);
            } else if (slips && !FloatComparator.equals(v2y + w2y * r2, v1y + w1y * r1)) {
                u1y = v1y - Tools.sign(v1y + w1y * r1) * fr * s / m1;
                u2y = v2y - Tools.sign(v2y + w2y * r2) * fr * s / m2;
                fw1y = w1y - Tools.sign(v1y + w1y * r1) * 2f * fr * s / (m1 * r1);
                fw2y = w2y - Tools.sign(v2y + w2y * r2) * 2f * fr * s / m2 / r2;
            } else if (!FloatComparator.equals(v2y + w2y * r2, v1y + w1y * r1)) {
                float avSpeed = (m2 * (v2y + w2y * r2) + m1 * (v1y + w1y * r1)) / (m1 + m2);
                u1y = (avSpeed + 2 * v1y - w1y * r1) / 3f;
                fw1y = (2 * u1y - 2 * v1y + w1y * r1) / r1;
                u2y = (avSpeed + 2 * v2y - w2y * r2) / 3f;
                fw2y = (2 * u2y - 2 * v2y + w2y * r2) / r2;
            }
            float u1x = ((ratio - k) / (ratio + 1)) * v1x + ((k + 1) / (ratio + 1)) * v2x;
            float u2x = ((ratio * (1 + k)) / (ratio + 1)) * v1x + ((1 - k * ratio) / (ratio + 1)) * v2x;
            final Vector2 fv1x = axisX.createByFloat(u1x);
            final Vector2 fv2x = axisX.createByFloat(u2x);
            final Vector2 fv1y = axisY.createByFloat(u1y);
            final Vector2 fv2y = axisY.createByFloat(u2y);
            sphere1.setW(Vector2.getConstByCrossProduct(axisY.createByFloat(fw1y * r1), radVector1));
            sphere2.setW(Vector2.getConstByCrossProduct(axisY.createByFloat(fw2y * r2), radVector2));
            sphere1.setV(new Vector2(fv1x, fv1y));
            sphere2.setV(new Vector2(fv2x, fv2y));
        }

    }

    private static void sphereToPolygon(Collisional thing1, Collisional thing2) {
        PhysicalPolygon polygon;
        ASS sphere;

        if (thing1 instanceof PhysicalPolygon) {
            polygon = (PhysicalPolygon) thing1;
            sphere = (ASS) thing2;
        } else {
            polygon = (PhysicalPolygon) thing2;
            sphere = (ASS) thing1;
        }
        Point2 collisionPoint = null;
        Line axisLine = null;
        for (Line line : polygon.getLines(true)) {
            SphereToLineIntersection sphereAndLinePair = new IntersectionalPair<>(sphere, new Wall(line, Material.Constantin)).getSphereToLineIntersection();
            if (new IntersectionalPair<>(sphere, new Wall(line, Material.Constantin)).getSphereToLineIntersection().isIntersected) {
                collisionPoint = sphereAndLinePair.getCollisionPoint();
                axisLine = line;
                break;
            }
        }
        if (collisionPoint != null) {
            final float k = Tools.countAverage(polygon.getMaterial().coefOfReduction, sphere.getMaterial().coefOfReduction);
            final float fr = Tools.countAverage(polygon.getMaterial().coefOfFriction, sphere.getMaterial().coefOfFriction);
            final Vector2 axisY = new Vector2(axisLine);
            final Vector2 axisX = axisY.createNormal();
            final float m1 = sphere.getM();
            final float m2 = polygon.getM();
            final float J2 = polygon.getJ();
            final float ratio = m1 / m2;
            final float v1x = sphere.getV().countProjectionOn(axisX);
            final float v2x = polygon.getV().countProjectionOn(axisX);
            final float v1y = sphere.getV().countProjectionOn(axisY);
            final float v2y = polygon.getV().countProjectionOn(axisY);
            final Vector2 polygonRadVector = new Vector2(polygon.getPositionOfCentre(true), collisionPoint);
            final float rx = Math.abs(polygonRadVector.countProjectionOn(axisX));
            final float ry = Math.abs(polygonRadVector.countProjectionOn(axisY));
            final float w2x = axisY.createByFloat(polygonRadVector.countProjectionOn(axisY)).getCrossProduct(polygon.getW()).countProjectionOn(axisX) / ry;
            final float u1x = ((ratio - k) * v1x + (v2x + w2x * ry) * (1 + k)) / (1 + ratio);
            final float s = m1 * (u1x - v1x);
            final float u2x = (-s / m2) + v2x;
            final float fw2x = ((- s * ry) / J2) + w2x;
//            System.out.println(polygon.getW());
//            System.out.println(w2x);
//            System.out.println(fw2x);
//            System.out.println("-------------");
//            System.out.println(ry);
//            System.out.println("=============");

            final Vector2 fv1x = axisX.createByFloat(u1x);
            final Vector2 fv1y = axisY.createByFloat(v1y);
            final Vector2 fv2x = axisX.createByFloat(u2x);
            final Vector2 fv2y = axisY.createByFloat(v2y);

            sphere.setV(new Vector2(fv1x, fv1y));
            polygon.setV(new Vector2(fv2x, fv2y));
            polygon.setW(polygon.getW() +
                    Vector2.getConstByCrossProduct(axisX.createByFloat(fw2x * ry), axisY.createByFloat(polygonRadVector.countProjectionOn(axisY))) -
                    Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * ry), axisY.createByFloat(polygonRadVector.countProjectionOn(axisY))));
        }
    }

    private static void polygonToWall(Collisional thing1, Collisional thing2) {
        PhysicalPolygon polygon;
        Wall wall;

        if (thing1 instanceof PhysicalPolygon) {
            polygon = (PhysicalPolygon) thing1;
            wall = (Wall) thing2;
        } else {
            polygon = (PhysicalPolygon) thing2;
            wall = (Wall) thing1;
        }

        final float k = Tools.countAverage(polygon.getMaterial().coefOfReduction, wall.getMaterial().coefOfReduction);
        final float fr = Tools.countAverage(polygon.getMaterial().coefOfFriction, wall.getMaterial().coefOfFriction);
        Point2 centre = polygon.getPositionOfCentre(true);
        Point2 c = null;
        ArrayList<Point2> points = polygon.getPoints(true);
        for (Point2 point : points) {
            if (new Line(point, centre).doesIntersectBySegmentsWith(wall)) {
                c = point;
                break;
            }
        }

        if (c != null) {
            Vector2 axisX = new Vector2(wall);
            Vector2 axisY = axisX.createNormal();
            Vector2 vel = polygon.getV();
            final float m = polygon.getM();
            final float J = polygon.getJ();
            float w = polygon.getW();
            if (vel.countProjectionOn(axisY) + new Vector2(centre, c).getCrossProduct(w).countProjectionOn(axisY) > 0)
                axisY.makeOp();
            Vector2 radVector = new Vector2(centre, c);
            final float rx = Math.abs(radVector.countProjectionOn(axisX));
            final float ry = Math.abs(radVector.countProjectionOn(axisY));
            final float v1y = vel.countProjectionOn(axisY);
            final float v1x = vel.countProjectionOn(axisX);
            final float w1y = axisX.createByFloat(radVector.countProjectionOn(axisX)).getCrossProduct(w).countProjectionOn(axisY) / rx;
            final float w2y = (J * w1y + rx * m * -(k * (v1y + w1y * rx) + v1y)) / (J + rx * rx * polygon.getM());
            final float s = J * (w2y - w1y) / rx;
            float v2y = v1y + s / m;
            polygon.setW(polygon.getW() +
                    Vector2.getConstByCrossProduct(axisY.createByFloat(w2y * rx), axisX.createByFloat(radVector.countProjectionOn(axisX))) -
                    Vector2.getConstByCrossProduct(axisY.createByFloat(w1y * rx), axisX.createByFloat(radVector.countProjectionOn(axisX))));
            float v2x = v1x;
            w = polygon.getW();
            float w1x = axisY.createByFloat(radVector.countProjectionOn(axisY)).getCrossProduct(w).countProjectionOn(axisX) / ry;
            float w2x = w1x;
            boolean slips = true;
            if (Math.abs(J * w1x * ry + J * v1x) * m / ((J + m * ry * ry) * s) < fr)
                slips = false;
            if (slips && !FloatComparator.equals(w1x * ry + v1x, 0f)) {
                v2x = v1x - Tools.sign(w1x * ry + v1x) * fr * s / m;
                w2x = w1x - Tools.sign(w1x * ry + v1x) * fr * s * ry / J;
            } else if (!FloatComparator.equals(w1x * ry + v1x, 0f)) {
                w2x = (J * w1x - m * v1x * ry) / (J + m * ry * ry);
                v2x = -w2x * ry;
            }
            polygon.setV(new Vector2(axisX.createByFloat(v2x), axisY.createByFloat(v2y)));
            polygon.setW(polygon.getW() +
                    Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * ry), axisY.createByFloat(radVector.countProjectionOn(axisY))) -
                    Vector2.getConstByCrossProduct(axisX.createByFloat(w1x * ry), axisY.createByFloat(radVector.countProjectionOn(axisY))));
        }
    }


}

