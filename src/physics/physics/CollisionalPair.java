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
        if (-0.5f * Math.abs(v1x + w1x * sphere.r) / (v1y * (1 + k) * 1.5f) < fr) slips = false;
        if (Math.signum(v1x) == Math.signum(w1x) && Math.signum(v1y) != 0 && slips) {
            v2x = v1x + Math.signum(v1x) * fr * v1y * (1 + k);
            w2x = w1x + Math.signum(w1x) * 2 * fr * v1y * (1 + k) / sphere.r;
        } else if (slips && Math.signum(v1y) != 0) {
            float sign = Math.signum(Math.abs(v1x) - Math.abs(w1x * sphere.r));
            v2x = v1x + Math.signum(v1x) * sign * fr * v1y * (1 + k);
            w2x = w1x + -2 * Math.signum(w1x) * sign * fr * v1y * (1 + k) / sphere.r;
        } else {
            v2x = (-0.5f * w1x * sphere.r + v1x) / 1.5f;
            w2x = -v2x / sphere.r;
        }
        sphere.w = Math.signum(w1x) == Math.signum(w2x) ? Math.signum(sphere.w) * Math.abs(w2x) : -Math.signum(sphere.w) * Math.abs(w2x);
        Vector2 fv1x = axisX.createByFloat(v2x);
        Vector2 fv1y = axisY.createByFloat(-v1y * k);
        sphere.v = new Vector2(fv1x, fv1y);
    }

    private void sphereToSphere(ASS sphere1, ASS sphere2) {
        Point2 thisPos = sphere1.getPosition(true);
        Point2 thingPos = sphere2.getPosition(true);
        Vector2 axisX = new Vector2(thisPos.x - thingPos.x,
                thisPos.y - thingPos.y);
        if (axisX.length() != 0.0) {
            Vector2 axisY = axisX.createNormal();
            float ratio = sphere1.m / sphere2.m;
            float k = Tools.countAverage(sphere1.material.coefOfReduction, sphere2.material.coefOfReduction);
            float v1x = sphere1.v.countProjectionOn(axisX);
            float v2x = sphere2.v.countProjectionOn(axisX);
            float v1y = sphere1.v.countProjectionOn(axisY);
            float v2y = sphere2.v.countProjectionOn(axisY);
            float u1x = ((ratio - k) / (ratio + 1)) * v1x + ((k + 1) / (ratio + 1)) * v2x;
            float u2x = ((ratio * (1 + k)) / (ratio + 1)) * v1x + ((1 - k * ratio) / (ratio + 1)) * v2x;
            Vector2 fv1x = axisX.createByFloat(u1x);
            Vector2 fv2x = axisX.createByFloat(u2x);
            Vector2 fv1y = axisY.createByFloat(v1y);
            Vector2 fv2y = axisY.createByFloat(v2y);
            sphere1.v = new Vector2(fv1x, fv1y);
            sphere2.v = new Vector2(fv2x, fv2y);
        }

    }

    private void sphereToPolygon(ASS sphere, AST triangle){

    }


}

