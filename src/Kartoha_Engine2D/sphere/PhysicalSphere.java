package Kartoha_Engine2D.sphere;

import Kartoha_Engine2D.geometry.*;
import Kartoha_Engine2D.limiters.Collisional;
import Kartoha_Engine2D.limiters.Intersectional;
import Kartoha_Engine2D.physics.Material;
import Kartoha_Engine2D.physics.Space;
import Kartoha_Engine2D.ui.Controllable;
import Kartoha_Engine2D.utils.ImageReader;
import Kartoha_Engine2D.utils.JsonAble;
import Kartoha_Engine2D.utils.Tools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PhysicalSphere extends Sphere2 implements Collisional, Intersectional, Controllable, JsonAble {

    private float x0, y0;
    private int z;
    private final float r;
    private Vector2 v;
    private float w;
    private final float J;
    private final Vector2 orientationVector;
    private final Space space;
    private final Material material;
    private final float m;
    private BufferedImage sprite;
    private float rotateAngle = 0;


    {
        sprite = null;
        z = 0;
    }


    public PhysicalSphere(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
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
        rotateAngle += space.getDT() * w;
        if (rotateAngle > Math.PI * 2){
            rotateAngle -= Math.PI * 2;
        }
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

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setSprite(String path) throws IOException {
        sprite = ImageReader.read(path);
    }

    @Override
    public void draw(Graphics g) {
        if (sprite == null) {
            g.setColor(material.outlineColor);
            int[] cords = new int[]{Tools.transformFloat(x0 - r - space.getCamera().getXMovement()), Tools.transformFloat(y0- space.getCamera().getYMovement() - getR()), Tools.transformFloat(r * 2)};
            g.drawOval(cords[0], cords[1], cords[2], cords[2]);
            g.drawLine(Tools.transformFloat(x0 - space.getCamera().getXMovement()),
                    Tools.transformFloat(y0 - space.getCamera().getYMovement()),
                    Tools.transformFloat(x0 - space.getCamera().getXMovement() + orientationVector.getX()),
                    Tools.transformFloat(y0 - space.getCamera().getYMovement() + orientationVector.getY()));

            g.setColor(Color.green);

            g.drawLine(Tools.transformFloat(x0 - space.getCamera().getXMovement()),
                    Tools.transformFloat(y0 - space.getCamera().getYMovement()),
                    Tools.transformFloat(x0 - space.getCamera().getXMovement() + v.getX() * space.getDT()),
                    Tools.transformFloat(y0 - space.getCamera().getYMovement() + v.getY() * space.getDT()));

            g.setColor(material.fillColor);
            g.fillOval(cords[0], cords[1], cords[2], cords[2]);
        }
        if (sprite != null){
            Graphics2D g2d = (Graphics2D) g;
            AABB aabb = new AABB(this, false);
            AffineTransform backup = g2d.getTransform();
            AffineTransform tx = AffineTransform.getRotateInstance(rotateAngle, x0, y0);
            g2d.setTransform(tx);

            g2d.drawImage(sprite, Tools.transformFloat(aabb.getMin().x - space.getCamera().getXMovement()) - 1, Tools.transformFloat(aabb.getMin().y- space.getCamera().getYMovement()) - 1,
                    Tools.transformFloat(aabb.getMax().x - aabb.getMin().x) + 2,
                    Tools.transformFloat(aabb.getMax().y - aabb.getMin().y) + 2, null);

            g2d.setTransform(backup);

        }
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

    @Override
    public void rotate(float a){
    }

    @Override
    public void move(Vector2 movement) {
        x0 += movement.getX();
        y0 += movement.getY();

    }

    @Override
    public void setCords(Point2 newCords) {
        x0 = newCords.x;
        y0 = newCords.y;
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

    @Override
    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}


