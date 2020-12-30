package physics.physics;


import physics.drawing.Primitive;
import physics.geometry.*;
import physics.sphere.ASS;
import physics.triangle.AST;
import physics.utils.Tools;

public class CollisionalPair<FirstThing extends Collisional, SecondThing extends Collisional> {
    private FirstThing firstThing;
    private SecondThing secondThing;

    public CollisionalPair(FirstThing firstThing, SecondThing secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }


    public void collide() {
        if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.WALL)
            sphereToWall((ASS) firstThing, (Wall) secondThing);
        else if (firstThing.getType() == Primitive.WALL && secondThing.getType() == Primitive.SPHERE)
            sphereToWall((ASS) secondThing, (Wall) firstThing);
        else if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.SPHERE)
            sphereToSphere((ASS) firstThing, (ASS) secondThing);
        else if (firstThing.getType() == Primitive.TRIANGLE && secondThing.getType() == Primitive.SPHERE)
            sphereToPolygon((ASS) secondThing, (AST) firstThing);
        else if (firstThing.getType() == Primitive.SPHERE && secondThing.getType() == Primitive.TRIANGLE)
            sphereToPolygon((ASS) firstThing, (AST) secondThing);
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
        sphere.w = Tools.sign(w1x) == Tools.sign(w2x) ? Tools.sign(sphere.w) * Math.abs(w2x) : -Tools.sign(sphere.w) * Math.abs(w2x);
        Vector2 fv1x = axisX.createByFloat(v2x);
        Vector2 fv1y = axisY.createByFloat(-v1y * k);
        sphere.v = new Vector2(fv1x, fv1y);
    }

    private void sphereToSphere(ASS sphere1, ASS sphere2) {
        Point2 firstSpherePos = sphere1.getPosition(true);
        Point2 secondSpherePos = sphere2.getPosition(true);
        Vector2 axisX = new Vector2(firstSpherePos.x - secondSpherePos.x,
                firstSpherePos.y - secondSpherePos.y);
        if (axisX.length() != 0.0) {
            Vector2 axisY = axisX.createNormal();
            float ratio = sphere1.m / sphere2.m;
            float k = Tools.countAverage(sphere1.material.coefOfReduction, sphere2.material.coefOfReduction);
            float fr = Tools.countAverage(sphere1.material.coefOfFriction, sphere2.material.coefOfFriction);
            float v1x = sphere1.v.countProjectionOn(axisX);
            float v2x = sphere2.v.countProjectionOn(axisX);
            float v1y = sphere1.v.countProjectionOn(axisY);
            float v2y = sphere2.v.countProjectionOn(axisY);
            float s = (sphere1.m * sphere2.m) / (sphere1.m + sphere2.m) * (1 + k) * Math.abs(v1x - v2x);
            Vector2 radVector1 = axisX.createByFloat(-sphere1.r);
            float w1y = radVector1.getCrossProduct(sphere1.w).countProjectionOn(axisY) / sphere1.r;
            Vector2 radVector2 = axisX.createByFloat(+sphere2.r);
            float w2y = radVector2.getCrossProduct(sphere2.w).countProjectionOn(axisY) / sphere2.r;
            boolean slips = true;
            if (Math.abs((v2y + w2y * sphere2.r) - (v1y + w1y * sphere1.r)) / (3 * s * Math.abs(1 / sphere1.m + 1 / sphere2.m)) < fr)
                slips = false;
            System.out.println(slips);
            float u1y = v1y, u2y = v2y;
            float fw1y = w1y, fw2y = w2y;
            if (Math.signum(v1y + w1y * sphere1.r) == Math.signum(v2y + w2y * sphere2.r) && slips) {
                float sign = Math.signum(Math.abs(v1y + w1y * sphere1.r) - Math.abs(v2y + w2y * sphere2.r));
                u1y = v1y - sign * Tools.sign(v1y + w1y * sphere1.r) * fr * s / sphere1.m;
                u2y = v2y + sign * Tools.sign(v2y + w2y * sphere2.r) * fr * s / sphere2.m;
                fw1y = w1y - sign * Tools.sign(v1y + w1y * sphere1.r) * 2 * fr * s / (sphere1.m * sphere1.r);
                fw2y = w2y + sign * Tools.sign(v2y + w2y * sphere2.r) * 2 * fr * s / (sphere2.m * sphere2.r);
            } else if (slips) {
                u1y = v1y - Tools.sign(v1y + w1y * sphere1.r) * fr * s / sphere1.m;
                u2y = v2y - Tools.sign(v2y + w2y * sphere2.r) * fr * s / sphere2.m;
                fw1y = w1y - Tools.sign(v1y + w1y * sphere1.r) * 2 * fr * s / (sphere1.m * sphere1.r);
                fw2y = w2y - Tools.sign(v2y + w2y * sphere2.r) * 2 * fr * s / (sphere2.m * sphere2.r);
            } else {
                float avSpeed = (sphere2.m * (v2y + w2y * sphere2.r) + sphere1.m * (v1y + w1y * sphere1.r)) / (sphere1.m + sphere2.m);
                u1y = (avSpeed + 2 * v1y - w1y * sphere1.r) / 3f;
                fw1y = (2 * u1y - 2 * v1y + w1y * sphere1.r) / sphere1.r;
                u2y = (avSpeed + 2 * v2y - w2y * sphere2.r) / 3f;
                fw2y = (2 * u2y - 2 * v2y + w2y * sphere2.r) / sphere2.r;

            }
            float u1x = v1x + s / sphere1.m;
            float u2x = v2x - s / sphere2.m;
            Vector2 fv1x = axisX.createByFloat(u1x);
            Vector2 fv2x = axisX.createByFloat(u2x);
            Vector2 fv1y = axisY.createByFloat(u1y);
            Vector2 fv2y = axisY.createByFloat(u2y);
            sphere1.w = Tools.sign(w1y) == Tools.sign(fw1y) ? Tools.sign(sphere1.w) * Math.abs(fw1y) : -Tools.sign(sphere1.w) * Math.abs(fw1y);
            sphere2.w = Tools.sign(w2y) == Tools.sign(fw2y) ? Tools.sign(sphere2.w) * Math.abs(fw2y) : -Tools.sign(sphere2.w) * Math.abs(fw2y);
            sphere1.v = new Vector2(fv1x, fv1y);
            sphere2.v = new Vector2(fv2x, fv2y);
        }

    }

    private void sphereToPolygon(ASS sphere, AST triangle) {

    }


}

