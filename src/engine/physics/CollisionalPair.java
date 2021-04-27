package engine.physics;


import engine.geometry.*;
import engine.limiters.Collisional;
import engine.sphere.PhysicalSphere;
import engine.polygons.PhysicalPolygon;
import engine.utils.FloatComparator;
import engine.utils.Tools;
import engine.utils.TripleMap;

import java.util.ArrayList;
import java.util.HashMap;

public final class CollisionalPair<FirstThingType extends Collisional, SecondThingType extends Collisional> {

    private final FirstThingType firstThing;
    private final SecondThingType secondThing;
    private final static TripleMap<Class, Class, Collider> methodsMap;

    public CollisionalPair(FirstThingType firstThing, SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }


    static {
        methodsMap = new TripleMap<>();

        methodsMap.addFirstKey(PhysicalSphere.class);
        methodsMap.addFirstKey(Wall.class);
        methodsMap.addFirstKey(PhysicalPolygon.class);

        methodsMap.putByFirstKey(PhysicalSphere.class, Wall.class, CollisionalPair::sphereToWall);
        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalSphere.class, CollisionalPair::sphereToSphere);
        methodsMap.putByFirstKey(PhysicalSphere.class, PhysicalPolygon.class, CollisionalPair::sphereToPolygon);

        methodsMap.putByFirstKey(Wall.class, PhysicalSphere.class, CollisionalPair::sphereToWall);
        methodsMap.putByFirstKey(Wall.class, PhysicalPolygon.class, CollisionalPair::polygonToWall);

        methodsMap.putByFirstKey(PhysicalPolygon.class, Wall.class, CollisionalPair::polygonToWall);
        methodsMap.putByFirstKey(PhysicalPolygon.class, PhysicalSphere.class, CollisionalPair::sphereToPolygon);
        methodsMap.putByFirstKey(PhysicalPolygon.class, PhysicalPolygon.class, CollisionalPair::polygonToPolygon);


    }


    public void collide() {
        methodsMap.getElement(firstThing.getClass(), secondThing.getClass()).collide(firstThing, secondThing);
    }

    private static void sphereToWall(Collisional thing1, Collisional thing2) {
        PhysicalSphere sphere;
        Wall wall;

        if (thing1 instanceof PhysicalSphere) {
            sphere = (PhysicalSphere) thing1;
            wall = (Wall) thing2;
        } else {
            wall = (Wall) thing1;
            sphere = (PhysicalSphere) thing2;
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

        float sDivM = fr * (1 + k) * Math.abs(v1y);
        final float r = sphere.getR();

        boolean slips = true;
        if ((Math.abs(0.5f * v1x + 0.5f * w1x * r) / (Math.abs(v1y) * (1 + k) * 1.5f)) < fr) slips = false;

        if (slips && !FloatComparator.equals(w1x * r + v1x, 0f)) {

            v2x = v1x - Tools.sign(w1x * r + v1x) * sDivM;
            w2x = w1x - 2 * Tools.sign(w1x * r + v1x) * sDivM / r;

        } else if (!FloatComparator.equals(w1x * r + v1x, 0f)) {

            v2x = (-0.5f * w1x * r + v1x) / 1.5f;
            w2x = -v2x / r;

        }

        Vector2 fv1x = axisX.createByFloat(v2x);
        Vector2 fv1y = axisY.createByFloat(-v1y * k);

        sphere.setW(Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * r), radVector));
        sphere.setV(new Vector2(fv1x, fv1y));
    }

    private static void sphereToSphere(Collisional thing1, Collisional thing2) {
        PhysicalSphere sphere1 = (PhysicalSphere) thing1;
        PhysicalSphere sphere2 = (PhysicalSphere) thing2;

        final Point2 firstSpherePos = sphere1.getPosition(false);
        final Point2 secondSpherePos = sphere2.getPosition(false);
        final Vector2 axisX = new Vector2(firstSpherePos.x - secondSpherePos.x,
                firstSpherePos.y - secondSpherePos.y);

        if (axisX.length() != 0.0f) {
            Vector2 axisY = axisX.createNormal();

            final float m1 = sphere1.getM();
            final float m2 = sphere2.getM();
            final float ratio = m1 / m2;
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

            boolean slips = !(Math.abs((v2y + w2y * r2) - (v1y + w1y * r1)) / (3f * s * Math.abs(1f / m1 + 1f / m2)) < fr);

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
                fw2y = w2y - Tools.sign(v2y + w2y * r2) * 2f * fr * s / (m2 * r2);

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
        PhysicalSphere sphere;

        if (thing1 instanceof PhysicalPolygon) {
            polygon = (PhysicalPolygon) thing1;
            sphere = (PhysicalSphere) thing2;
        } else {
            polygon = (PhysicalPolygon) thing2;
            sphere = (PhysicalSphere) thing1;
        }
        Point2 collisionPoint;
        Line axisLine;
        HashMap<Line, Point2> collisionParams = new HashMap<>();
        for (Line line : polygon.getLines(true)) {
            SphereToLineIntersection sphereAndLinePair = new IntersectionalPair<>(sphere, new Wall(line, Material.Constantin)).getSphereToLineIntersection();
            if (new IntersectionalPair<>(sphere, new Wall(line, Material.Constantin)).getSphereToLineIntersection().isIntersected) {
                collisionParams.put(line, sphereAndLinePair.getCollisionPoint());
            }
        }
        for (var param : collisionParams.entrySet()) {

            axisLine = param.getKey();
            collisionPoint = param.getValue();

            final float k = Tools.countAverage(polygon.getMaterial().coefOfReduction, sphere.getMaterial().coefOfReduction);
            final float fr = Tools.countAverage(polygon.getMaterial().coefOfFriction, sphere.getMaterial().coefOfFriction);

            final Vector2 axisY = new Vector2(axisLine);
            final Vector2 axisX = axisY.createNormal();

            final float m1 = sphere.getM();
            final float m2 = polygon.getM();
            final float J2 = polygon.getJ();
            final float J1 = sphere.getJ();
            final float ratio = m1 / m2;

            final float v1x = sphere.getV().countProjectionOn(axisX);
            final float v2x = polygon.getV().countProjectionOn(axisX);
            final float v1y = sphere.getV().countProjectionOn(axisY);
            final float v2y = polygon.getV().countProjectionOn(axisY);

            final Vector2 polygonRadVector = new Vector2(polygon.getPositionOfCentre(true), collisionPoint);
            final Vector2 sphereRadVector = new Vector2(sphere.getPosition(true), collisionPoint);
            sphereRadVector.setLength(sphere.getR());

            final float rx = Math.abs(polygonRadVector.countProjectionOn(axisX));
            float ry = Math.abs(polygonRadVector.countProjectionOn(axisY));
            if (ry == 0){
                ry += 0.01;
            }
            final float r = sphere.getR();

            final float w2x = axisY.createByFloat(polygonRadVector.countProjectionOn(axisY)).getCrossProduct(polygon.getW()).countProjectionOn(axisX) / ry;
            final float u1x = ((ratio - k) * v1x + (v2x + w2x * ry) * (1 + k) + m1 * ry * ry * v1x / J2 + w2x * ry) / (1 + ratio + m1 * ry * ry / J2);
            final float s = m1 * (u1x - v1x);
            final float u2x = (-s / m2) + v2x;
            final float fw2x = ((-s * ry) / J2) + w2x;


            final float w1y = sphereRadVector.getCrossProduct(sphere.getW()).countProjectionOn(axisY) / sphere.getR();
            final float w2y = axisX.createByFloat(polygonRadVector.countProjectionOn(axisX)).getCrossProduct(polygon.getW()).countProjectionOn(axisY) / rx;

            polygon.setW(polygon.getW() +
                    Vector2.getConstByCrossProduct(axisX.createByFloat(fw2x * ry), axisY.createByFloat(polygonRadVector.countProjectionOn(axisY))) -
                    Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * ry), axisY.createByFloat(polygonRadVector.countProjectionOn(axisY))));

            float u1y = v1y;
            float u2y = v2y;
            float fw1y = w1y;
            float fw2y = w2y;


            float diff = v1y + w1y * r - v2y - w2y * rx;
            if (Math.signum(v1y + w1y * r) == Math.signum(v2y + w2y * rx) && !FloatComparator.equals(v2y + w2y * rx, v1y + w1y * r)) {

                float sign = Math.signum(Math.abs(v1y + w1y * r) - Math.abs(v2y + w2y * rx));
//                System.out.println(v1y + w1y * r);
//                System.out.println(v2y + w2y * rx);
                u1y = v1y - sign * Tools.sign(v1y + w1y * r) * fr * Math.abs(s) / m1;
                u2y = v2y + sign * Tools.sign(v2y + w2y * rx) * fr * Math.abs(s) / m2;
                fw1y = w1y - sign * Tools.sign(v1y + w1y * r) * 2f * fr * Math.abs(s) / (m1 * r);
                fw2y = w2y + sign * Tools.sign(v2y + w2y * rx) * fr * Math.abs(s * rx) / J2;
//                System.out.println(u1y + fw1y * r);
//                System.out.println(u2y + fw2y * rx);

            } else if (!FloatComparator.equals(v2y + w2y * rx, v1y + w1y * r)) {

                u1y = v1y - Tools.sign(v1y + w1y * r) * fr * Math.abs(s) / m1;
                u2y = v2y - Tools.sign(v2y + w2y * rx) * fr * Math.abs(s) / m2;
                fw1y = w1y - Tools.sign(v1y + w1y * r) * 2f * fr * Math.abs(s) / (m1 * r);
                fw2y = w2y - Tools.sign(v2y + w2y * rx) * fr * Math.abs(s * rx) / J2;
//                System.out.println(2);

            }

            if (Math.signum(diff) != Math.signum(u1y + fw1y * r - u2y - fw2y * rx)) {

                fw1y = (-v1y + (J1 / (m1 * r)) * (ratio + 1) * w1y + v2y + w2y * rx + (J1 * rx * rx / (J2 * r)) * w1y)
                        / ((J1 / (m1 * r)) * (ratio + 1) + r + J1 * rx * rx / (J2 * r));
                u1y = v1y + J1 * fw1y / (m1 * r) - J1 * w1y / (m1 * r);
                u2y = v2y + ratio * (v1y - u1y);
                fw2y = w2y + (-fw1y + w1y) * J1 * rx / (J2 * r);


            }

            final Vector2 fv1x = axisX.createByFloat(u1x);
            final Vector2 fv1y = axisY.createByFloat(u1y);
            final Vector2 fv2x = axisX.createByFloat(u2x);
            final Vector2 fv2y = axisY.createByFloat(u2y);

            polygon.setW(polygon.getW() +
                    Vector2.getConstByCrossProduct(axisY.createByFloat(fw2y * rx), axisX.createByFloat(polygonRadVector.countProjectionOn(axisX))) -
                    Vector2.getConstByCrossProduct(axisY.createByFloat(w2y * rx), axisX.createByFloat(polygonRadVector.countProjectionOn(axisX))));


            sphere.setW(Vector2.getConstByCrossProduct(axisY.createByFloat(fw1y * r), sphereRadVector));
            sphere.setV(new Vector2(fv1x, fv1y));
            polygon.setV(new Vector2(fv2x, fv2y));
        }
        polygon.setW(0);
        polygon.setV(new Vector2(0,0));
    }

    private static void polygonToPolygon(Collisional thing1, Collisional thing2) {

        PhysicalPolygon polygon1 = (PhysicalPolygon) thing1;
        PhysicalPolygon polygon2 = (PhysicalPolygon) thing2;

        Point2 centre = polygon1.getPositionOfCentre(true);
        Point2 collisionPoint = null;
        Line axisLine = null;

        for (Line line : polygon2.getLines(true)) {
            for (Point2 point : polygon1.getPoints()) {
                if (new Line(point, centre).doesIntersectBySegmentsWith(line)) {
                    collisionPoint = point;
                    axisLine = line;
                    break;
                }
            }
        }

        if (collisionPoint != null) {

            final float k = Tools.countAverage(polygon1.getMaterial().coefOfReduction, polygon2.getMaterial().coefOfReduction);
            final float fr = Tools.countAverage(polygon1.getMaterial().coefOfFriction, polygon2.getMaterial().coefOfFriction);

            final Vector2 axisY = new Vector2(axisLine);
            final Vector2 axisX = axisY.createNormal();

            final float m1 = polygon1.getM();
            final float m2 = polygon2.getM();
            final float J2 = polygon2.getJ();
            final float J1 = polygon1.getJ();
            final float ratio = m1 / m2;

            final float v1x = polygon1.getV().countProjectionOn(axisX);
            final float v2x = polygon2.getV().countProjectionOn(axisX);
            final float v1y = polygon1.getV().countProjectionOn(axisY);
            final float v2y = polygon2.getV().countProjectionOn(axisY);

            final Vector2 radVector1 = new Vector2(polygon1.getPositionOfCentre(true), collisionPoint);
            final Vector2 radVector2 = new Vector2(polygon2.getPositionOfCentre(true), collisionPoint);

            final float rx1 = Math.abs(radVector1.countProjectionOn(axisX));
            final float rx2 = Math.abs(radVector2.countProjectionOn(axisX));
            final float ry1 = Math.abs(radVector1.countProjectionOn(axisY));
            final float ry2 = Math.abs(radVector2.countProjectionOn(axisY));


            final float w1x = axisY.createByFloat(radVector1.countProjectionOn(axisY)).getCrossProduct(polygon1.getW()).countProjectionOn(axisX) / ry1;
            final float w2x = axisY.createByFloat(radVector2.countProjectionOn(axisY)).getCrossProduct(polygon2.getW()).countProjectionOn(axisX) / ry2;

            final float fw1x = (-k * (v1x + w1x * ry1 - v2x + w2x * ry2) + v2x - v1x + (ratio + 1) * J1 * w1x / (m1 * ry1) + w2x * ry2 + J1 * ry2 * ry2 * w1x / (J2 * ry1)) /
                    ((ratio + 1) * J1 / (m1 * ry1) + ry1 + J1 * ry2 * ry2 / (J2 * ry1));

            final float s = J1 * (fw1x - w1x) / ry1;
            final float u1x = v1x + s / m1;
             
            final float u2x = v2x - s / m2;
            final float fw2x = s * ry2 / J2 + w2x;


            final float w1y = axisX.createByFloat(radVector1.countProjectionOn(axisX)).getCrossProduct(polygon1.getW()).countProjectionOn(axisY) / rx1;
            final float w2y = axisX.createByFloat(radVector2.countProjectionOn(axisX)).getCrossProduct(polygon2.getW()).countProjectionOn(axisY) / rx2;

            polygon1.setW(polygon1.getW() +
                    Vector2.getConstByCrossProduct(axisX.createByFloat(fw1x * ry1), axisY.createByFloat(radVector1.countProjectionOn(axisY))) -
                    Vector2.getConstByCrossProduct(axisX.createByFloat(w1x * ry1), axisY.createByFloat(radVector1.countProjectionOn(axisY))));

            polygon2.setW(polygon2.getW() +
                    Vector2.getConstByCrossProduct(axisX.createByFloat(fw2x * ry2), axisY.createByFloat(radVector2.countProjectionOn(axisY))) -
                    Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * ry2), axisY.createByFloat(radVector2.countProjectionOn(axisY))));

            float u1y = v1y;
            float u2y = v2y;
            float fw1y = w1y;
            float fw2y = w2y;


            float diff = v1y + w1y * rx1 - v2y - w2y * rx2;
            if (Math.signum(v1y + w1y * rx1) == Math.signum(v2y + w2y * rx2) && !FloatComparator.equals(v2y + w2y * rx2, v1y + w1y * rx1)) {

                float sign = Math.signum(Math.abs(v1y + w1y * rx1) - Math.abs(v2y + w2y * rx2));
                u1y = v1y - sign * Tools.sign(v1y + w1y * rx1) * fr * Math.abs(s) / m1;
                u2y = v2y + sign * Tools.sign(v2y + w2y * rx2) * fr * Math.abs(s) / m2;
                fw1y = w1y - sign * Tools.sign(v1y + w1y * rx1) * fr * Math.abs(s * rx1) / J1;
                fw2y = w2y + sign * Tools.sign(v2y + w2y * rx2) * fr * Math.abs(s * rx2) / J2;

            } else if (!FloatComparator.equals(v2y + w2y * rx2, v1y + w1y * rx1)) {

                u1y = v1y - Tools.sign(v1y + w1y * rx1) * fr * Math.abs(s) / m1;
                u2y = v2y - Tools.sign(v2y + w2y * rx2) * fr * Math.abs(s) / m2;
                fw1y = w1y - Tools.sign(v1y + w1y * rx1) * fr * Math.abs(s * rx1) / J1;
                fw2y = w2y - Tools.sign(v2y + w2y * rx2) * fr * Math.abs(s * rx2) / J2;

            }

            if (Math.signum(diff) != Math.signum(u1y + fw1y * rx1 - u2y - fw2y * rx2)) {

                fw1y = (-v1y + (J1 / (m1 * rx1)) * (ratio + 1) * w1y + v2y + w2y * rx2 + (J1 * rx2 * rx2 / (J2 * rx1)) * w1y)
                        / ((J1 / (m1 * rx1)) * (ratio + 1) + rx1 + J1 * rx2 * rx2 / (J2 * rx1));


                u1y = v1y + J1 * fw1y / (m1 * rx1) - J1 * w1y / (m1 * rx1);
                u2y = v2y + ratio * (v1y - u1y);
                fw2y = w2y + (-fw1y + w1y) * J1 * rx2 / (J2 * rx1);


            }

            final Vector2 fv1x = axisX.createByFloat(u1x);
            final Vector2 fv1y = axisY.createByFloat(u1y);
            final Vector2 fv2x = axisX.createByFloat(u2x);
            final Vector2 fv2y = axisY.createByFloat(u2y);

            polygon1.setW(polygon1.getW() +
                    Vector2.getConstByCrossProduct(axisY.createByFloat(fw1y * rx1), axisX.createByFloat(radVector1.countProjectionOn(axisX))) -
                    Vector2.getConstByCrossProduct(axisY.createByFloat(w1y * rx1), axisX.createByFloat(radVector1.countProjectionOn(axisX))));

            polygon2.setW(polygon2.getW() +
                    Vector2.getConstByCrossProduct(axisY.createByFloat(fw2y * rx2), axisX.createByFloat(radVector2.countProjectionOn(axisX))) -
                    Vector2.getConstByCrossProduct(axisY.createByFloat(w2y * rx2), axisX.createByFloat(radVector2.countProjectionOn(axisX))));


            polygon1.setV(new Vector2(fv1x, fv1y));
            polygon2.setV(new Vector2(fv2x, fv2y));

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

            final float k = Tools.countAverage(polygon.getMaterial().coefOfReduction, wall.getMaterial().coefOfReduction);
            final float fr = Tools.countAverage(polygon.getMaterial().coefOfFriction, wall.getMaterial().coefOfFriction);

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

            w = polygon.getW();

            float v2x = v1x;
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

