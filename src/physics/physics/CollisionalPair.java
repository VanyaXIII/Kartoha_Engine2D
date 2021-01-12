package physics.physics;


import physics.geometry.*;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;
import physics.utils.Tools;

import java.util.ArrayList;

public class CollisionalPair<FirstThingType extends Collisional, SecondThingType extends Collisional> {
    private final FirstThingType firstThing;
    private final SecondThingType secondThing;

    public CollisionalPair(FirstThingType firstThing, SecondThingType secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }


    public void collide() {
        if (firstThing instanceof ASS && secondThing instanceof Wall)
            sphereToWall((ASS) firstThing, (Wall) secondThing);
        else if (firstThing instanceof Wall && secondThing instanceof ASS)
            sphereToWall((ASS) secondThing, (Wall) firstThing);
        else if (firstThing instanceof ASS && secondThing instanceof ASS)
            sphereToSphere((ASS) firstThing, (ASS) secondThing);
        else if (firstThing instanceof PhysicalPolygon && secondThing instanceof ASS)
            sphereToPolygon((ASS) secondThing, (PhysicalPolygon) firstThing);
        else if (firstThing instanceof ASS && secondThing instanceof PhysicalPolygon)
            sphereToPolygon((ASS) firstThing, (PhysicalPolygon) secondThing);
        else if (firstThing instanceof PhysicalPolygon && secondThing instanceof Wall)
            polygonToWall((PhysicalPolygon) firstThing, (Wall) secondThing);
        else if (secondThing instanceof PhysicalPolygon && firstThing instanceof Wall)
            polygonToWall((PhysicalPolygon) secondThing, (Wall) firstThing);
    }

    private void sphereToWall(ASS sphere, Wall wall) {
        Vector2 axisX = new Vector2(wall);
        Vector2 axisY = axisX.createNormal();
        float fr = Tools.countAverage(sphere.material.coefOfFriction, wall.material.coefOfFriction);
        float k = Tools.countAverage(sphere.material.coefOfReduction, wall.material.coefOfReduction);
        if (sphere.v.countProjectionOn(axisY) > 0f) axisY.makeOp();
        Vector2 radVector = axisY.createByFloat(-sphere.r);
        float w1x = radVector.getCrossProduct(sphere.w).countProjectionOn(axisX) / sphere.r;
        float v1x = sphere.v.countProjectionOn(axisX);
        float v1y = sphere.v.countProjectionOn(axisY);
        float v2x;
        float w2x;
        boolean slips = true;
        float sDivM = fr * (1 + k) * Math.abs(v1y);
        if ((Math.abs(0.5f * v1x + 0.5f * w1x * sphere.r) / (Math.abs(v1y) * (1 + k) * 1.5f)) < fr) slips = false;
        if (slips) {
            v2x = v1x - Tools.sign(w1x * sphere.r + v1x) * sDivM;
            w2x = w1x - 2 * Tools.sign(w1x * sphere.r + v1x) * sDivM / sphere.r;
        } else {
            v2x = (-0.5f * w1x * sphere.r + v1x) / 1.5f;
            w2x = -v2x / sphere.r;
        }
        sphere.w = Vector2.getConstByCrossProduct(axisX.createByFloat(w2x * sphere.r), radVector);
        Vector2 fv1x = axisX.createByFloat(v2x);
        Vector2 fv1y = axisY.createByFloat(-v1y * k);
        sphere.v = new Vector2(fv1x, fv1y);
    }

    private void sphereToSphere(ASS sphere1, ASS sphere2) {
        Point2 firstSpherePos = sphere1.getPosition(false);
        Point2 secondSpherePos = sphere2.getPosition(false);
        Vector2 axisX = new Vector2(firstSpherePos.x - secondSpherePos.x,
                firstSpherePos.y - secondSpherePos.y);
        if (axisX.length() != 0.0f) {
            float ratio = sphere1.m / sphere2.m;
            Vector2 axisY = axisX.createNormal();
            float k = Tools.countAverage(sphere1.material.coefOfReduction, sphere2.material.coefOfReduction);
            float fr = Tools.countAverage(sphere1.material.coefOfFriction, sphere2.material.coefOfFriction);
            float v1x = sphere1.v.countProjectionOn(axisX);
            float v2x = sphere2.v.countProjectionOn(axisX);
            float v1y = sphere1.v.countProjectionOn(axisY);
            float v2y = sphere2.v.countProjectionOn(axisY);
            float s = (sphere1.m * sphere2.m) / (sphere1.m + sphere2.m) * (1f + k) * Math.abs(v1x - v2x);
            Vector2 radVector1 = axisX.createByFloat(-sphere1.r);
            float w1y = radVector1.getCrossProduct(sphere1.w).countProjectionOn(axisY) / sphere1.r;
            Vector2 radVector2 = axisX.createByFloat(+sphere2.r);
            float w2y = radVector2.getCrossProduct(sphere2.w).countProjectionOn(axisY) / sphere2.r;
            boolean slips = true;
            if (Math.abs((v2y + w2y * sphere2.r) - (v1y + w1y * sphere1.r)) / (3f * s * Math.abs(1f / sphere1.m + 1f / sphere2.m)) < fr)
                slips = false;
            float u1y, u2y;
            float fw1y, fw2y;
            if (Math.signum(v1y + w1y * sphere1.r) == Math.signum(v2y + w2y * sphere2.r) && slips) {
                float sign = Math.signum(Math.abs(v1y + w1y * sphere1.r) - Math.abs(v2y + w2y * sphere2.r));
                u1y = v1y - sign * Tools.sign(v1y + w1y * sphere1.r) * fr * s / sphere1.m;
                u2y = v2y + sign * Tools.sign(v2y + w2y * sphere2.r) * fr * s / sphere2.m;
                fw1y = w1y - sign * Tools.sign(v1y + w1y * sphere1.r) * 2f * fr * s / (sphere1.m * sphere1.r);
                fw2y = w2y + sign * Tools.sign(v2y + w2y * sphere2.r) * 2f * fr * s / (sphere2.m * sphere2.r);
            } else if (slips) {
                u1y = v1y - Tools.sign(v1y + w1y * sphere1.r) * fr * s / sphere1.m;
                u2y = v2y - Tools.sign(v2y + w2y * sphere2.r) * fr * s / sphere2.m;
                fw1y = w1y - Tools.sign(v1y + w1y * sphere1.r) * 2f * fr * s / (sphere1.m * sphere1.r);
                fw2y = w2y - Tools.sign(v2y + w2y * sphere2.r) * 2f * fr * s / (sphere2.m * sphere2.r);
            } else {
                float avSpeed = (sphere2.m * (v2y + w2y * sphere2.r) + sphere1.m * (v1y + w1y * sphere1.r)) / (sphere1.m + sphere2.m);
                u1y = (avSpeed + 2 * v1y - w1y * sphere1.r) / 3f;
                fw1y = (2 * u1y - 2 * v1y + w1y * sphere1.r) / sphere1.r;
                u2y = (avSpeed + 2 * v2y - w2y * sphere2.r) / 3f;
                fw2y = (2 * u2y - 2 * v2y + w2y * sphere2.r) / sphere2.r;
            }
            float u1x = ((ratio - k) / (ratio + 1)) * v1x + ((k + 1) / (ratio + 1)) * v2x;
            float u2x = ((ratio * (1 + k)) / (ratio + 1)) * v1x + ((1 - k * ratio) / (ratio + 1)) * v2x;
            Vector2 fv1x = axisX.createByFloat(u1x);
            Vector2 fv2x = axisX.createByFloat(u2x);
            Vector2 fv1y = axisY.createByFloat(u1y);
            Vector2 fv2y = axisY.createByFloat(u2y);
            sphere1.w = Vector2.getConstByCrossProduct(axisY.createByFloat(fw1y * sphere1.r), radVector1);
            sphere2.w = Vector2.getConstByCrossProduct(axisY.createByFloat(fw2y * sphere2.r), radVector2);
            sphere1.v = new Vector2(fv1x, fv1y);
            sphere2.v = new Vector2(fv2x, fv2y);
        }

    }

    private void sphereToPolygon(ASS sphere, PhysicalPolygon triangle) {

    }

    private void polygonToWall(PhysicalPolygon polygon, Wall wall) {
        float k = Tools.countAverage(polygon.material.coefOfReduction, wall.material.coefOfReduction);
        float fr = Tools.countAverage(polygon.material.coefOfFriction, wall.material.coefOfFriction);
        Point2 centre = polygon.getPositionOfCentre(true);
        ArrayList<Point2> collisionPoints = new ArrayList<>();
        ArrayList<Point2> points = polygon.getPoints(true);
        for (Point2 point : points) {
            if (new Line(point, centre).doesIntersectBySegmentsWith(wall)) {
                collisionPoints.add(point);
            }
        }
        Vector2 axisX = new Vector2(wall);
        Vector2 axisY = axisX.createNormal();
        for (Point2 c : collisionPoints) {
            if (polygon.v.countProjectionOn(axisY) + new Vector2(centre, c).getCrossProduct(polygon.w).countProjectionOn(axisY) > 0)
                axisY.makeOp();
            Vector2 radVector = new Vector2(centre, c);
            float rx = Math.abs(radVector.countProjectionOn(axisX));
            float v1y = polygon.v.countProjectionOn(axisY);
            float v1x = polygon.v.countProjectionOn(axisX);
            float w1y = axisX.createByFloat(radVector.countProjectionOn(axisX)).getCrossProduct(polygon.w).countProjectionOn(axisY) / rx;
            float w2y = (polygon.J * w1y + rx * polygon.m * (-k * (v1y + w1y * rx) - v1y)) / (polygon.J + rx * rx * polygon.m);
            float s = polygon.J * (w2y - w1y) / rx;
            float v2y = v1y + s / polygon.m;

            polygon.v = new Vector2(axisX.createByFloat(v1x), axisY.createByFloat(v2y));
            polygon.w = Vector2.getConstByCrossProduct(axisY.createByFloat(w2y * rx), axisX.createByFloat(radVector.countProjectionOn(axisX)));
        }
    }


}

