package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.*;
import physics.limiters.Collisional;
import physics.limiters.Intersectional;
import physics.physics.Material;
import physics.physics.Space;
import physics.utils.Tools;

import java.awt.*;

public class ASS extends Sphere2D implements Drawable, Collisional, Intersectional {

    private float x0, y0;
    private final float r;
    private Vector2 v;
    private float w;
    private final float J;
    private final Vector2 orientationVector;
    private final Space space;
    private final Material material;
    private final float m;


    public ASS(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
        super(x0, y0, r);
        this.x0 = x0;
        this.r = r;
        this.y0 = y0;
        this.space = space;
        this.v = v;
        this.w = w;
        this.material = material;
        this.m = ((float)Math.PI * r * r / 2) * material.p;
        J = 0.5f * m * r * r;
        orientationVector = new Vector2(0, r);
    }

    public synchronized void update() {
        changeSpeed();
        rotate();
        x0 += v.getX() * space.getDT();
        y0 += (v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT() / 2.0f;

    }

    private synchronized void changeSpeed() {
        v.addY(space.getG() * space.getDT());
    }


    private synchronized void rotate() {
        orientationVector.rotate(w * space.getDT());
    }


    public synchronized void pullFromSphere(SpheresIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point2 nCords = intersection.centralLine.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }

    public synchronized void pullFromLine(SphereToLineIntersection intersection) {
        if (intersection.getValue() != 0){
            Vector2 movementVector = new Vector2(intersection.getCollisionPoint(), getPosition(false));
            Point2 nCords = movementVector.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }


    public synchronized Point2 getPosition(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        int[] cords = new int[]{Tools.transformFloat(x0 - r), Tools.transformFloat(y0 - getR()), Tools.transformFloat(r * 2)};
        g.drawOval(cords[0], cords[1], cords[2], cords[2]);
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + orientationVector.getX()),
                Tools.transformFloat(y0 + orientationVector.getY()));

        g.setColor(Color.green);

        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + v.getX() * space.getDT()),
                Tools.transformFloat(y0 + v.getY() * space.getDT()));

        g.setColor(material.fillColor);
        g.fillOval(cords[0], cords[1], cords[2], cords[2]);
    }

    public Vector2 getV() {
        return v;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setV(Vector2 v) {
        this.v = v;
    }

    public Material getMaterial() {
        return material;
    }

    public float getM() {
        assert m == 0: "Mass is null";
        return m;
    }

    public float getJ() {
        return J;
    }

    public float getR() {
        assert r == 0: "Radius is null";
        return r;
    }
}


