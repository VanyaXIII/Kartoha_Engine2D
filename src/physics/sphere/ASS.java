package physics.sphere;

import physics.drawing.Drawable;
import physics.drawing.Primitive;
import physics.geometry.*;
import physics.physics.Energy;
import physics.physics.Material;
import physics.physics.Space;
import physics.physics.Wall;
import physics.triangle.AST;
import physics.utils.Tools;

import java.awt.*;
//TODO сначала у одного шарик меняется скорость, а потом уже запускается расчет для другого, с учетом изменения скорости первого
//TODO надо все разюить по методам, чтобы сначала у всех менялась скорость
//TODO сделать просчет траектории
//TODO доделать коллизию, чтобы учитывались прямые

public class ASS extends Sphere2D implements Drawable, Collisional {
    private float x0, y0;
    public Vector2 v;
    public float w;
    public static boolean collisionMode = true;
    private Vector2 orientationVector;
    private final Space space;
    private boolean flag = true;

    // TODO подумать над полем пространства
    {
        orientationVector = new Vector2(0, r);
    }

    public ASS(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
        super(x0, y0, r, material);
        this.x0 = x0;
        this.y0 = y0;
        this.space = space;
        this.v = v;
        this.w = w + 0.01f;
    }

    public void changeCord() {
        changeSpeed();
        rotate();
        x0 += v.getX() * space.getDT();
        y0 += (v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT() / 2.0f;

    }

    private void changeSpeed() {
        flag = true;
        processSceneCollision();
        processCollision();
        v.addY(space.getG() * space.getDT());
        processCollision();
        processSceneCollision();
    }

    private void processSceneCollision() {
        if (((y0 - (r) + v.getY() * space.getDT() > 0.0) && (x0 + r + v.getX() * space.getDT()) > space.width) || (x0 + v.getX() * space.getDT() - r < 0.0)) {
            v.makeOpX();
        }
        if ((x0 - r + v.getX() * space.getDT() > 0.0) && (y0 - r + v.getY() * space.getDT() < 0.0)) {
            v.makeOpY();
        }
        if ((x0 + v.getX() * space.getDT() + r > 0.0) && (y0 + v.getY() * space.getDT() + r > space.height)) {
            v.makeOpY();
        }
    }

    private void rotate() {
        orientationVector.rotate(w * space.getDT());
    }

    private void processCollision() {
        for (ASS thing : space.countableSpheres) {
            if (!thing.equals(this) && new Pair<ASS, ASS>(this, thing, true).isIntersected()) {
                processSphereCollision(thing);
            }
            if (collisionMode) {
                SphereIntersection spheres = new Pair<ASS, ASS>(this, thing, false).getSphereIntersection();
                if (spheres.isIntersected) {
                    pullSpheres(spheres);
                }
            }
        }
        space.countableSpheres.remove(this);

        for (Wall wall : space.walls) {
            if (new Pair<ASS, Wall>(this, wall, true).isIntersected()) {
                processWallCollision(wall);
                flag = false;
            }
        }
        for (AST triangle : space.triangles) {
            synchronized (triangle) {
                if (new Pair<ASS, AST>(this, triangle, true).isIntersected()) x0=100;
            }
        }
    }

    private void pullSpheres(SphereIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point2 nCords = intersection.centralLine.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }



    private void processWallCollision(Wall wall) {
        Vector2 axisX = new Vector2(wall);
        Vector2 axisY = axisX.createNormal();
        float fr = Tools.countAverage(material.coefOfFriction, wall.material.coefOfFriction);
        float k = Tools.countAverage(material.coefOfReduction, wall.material.coefOfReduction);
        if (v.countProjectionOn(axisY) > 0f) axisY.makeOp();
        Vector2 radVector = axisY.createByFloat(-r);
        float w1x = radVector.getCrossProduct(w).countProjectionOn(axisX) / r;
        float v1x = v.countProjectionOn(axisX);
        float v1y = v.countProjectionOn(axisY);
        float v2x;
        float w2x;
        boolean slips = true;
        if (-0.5f * Math.abs(v1x + w1x * r) / (v1y * (1 + k) * 1.5f) < fr) slips = false;
        if (Math.signum(v1x) == Math.signum(w1x) && Math.signum(v1y) != 0 && slips) {
            v2x = v1x + Math.signum(v1x) * fr * v1y * (1 + k);
            w2x = w1x + Math.signum(w1x) * 2 * fr * v1y * (1 + k) / r;
        } else if (slips && Math.signum(v1y) != 0) {
            float sign = Math.signum(Math.abs(v1x) - Math.abs(w1x * r));
            v2x = v1x + Math.signum(v1x) * sign * fr * v1y * (1 + k);
            w2x = w1x + -2 * Math.signum(w1x) * sign * fr * v1y * (1 + k) / r;
        } else {
            v2x = (-0.5f * w1x * r + v1x) / 1.5f;
            w2x = -v2x / r;
        }
        w = Math.signum(w1x) == Math.signum(w2x) ? Math.signum(w) * Math.abs(w2x) : -Math.signum(w) * Math.abs(w2x);
        Vector2 fv1x = axisX.createByFloat(v2x);
        Vector2 fv1y = axisY.createByFloat(-v1y * k);
        v = new Vector2(fv1x, fv1y);
    }

    //TODO зафигачить перемещение при столкновении до положения столкновения
    private void processSphereCollision(ASS thing) {
        Point2 thisPos = this.getPosition(true);
        Point2 thingPos = thing.getPosition(true);
        Vector2 axisX = new Vector2(thisPos.x - thingPos.x,
                thisPos.y - thingPos.y);
        if (axisX.length() != 0.0) {
            Vector2 axisY = axisX.createNormal();
            float ratio = this.m / thing.m;
            float k = Tools.countAverage(this.material.coefOfReduction, thing.material.coefOfReduction);
//            System.out.println(k);
            float v1x = this.v.countProjectionOn(axisX);
            float v2x = thing.v.countProjectionOn(axisX);
            float v1y = this.v.countProjectionOn(axisY);
            float v2y = thing.v.countProjectionOn(axisY);
            float u1x = ((ratio - k) / (ratio + 1)) * v1x + ((k + 1) / (ratio + 1)) * v2x;
            float u2x = ((ratio * (1 + k)) / (ratio + 1)) * v1x + ((1 - k * ratio) / (ratio + 1)) * v2x;
            Vector2 fv1x = axisX.createByFloat(u1x);
            Vector2 fv2x = axisX.createByFloat(u2x);
            Vector2 fv1y = axisY.createByFloat(v1y);
            Vector2 fv2y = axisY.createByFloat(v2y);
            this.v = new Vector2(fv1x, fv1y);
            thing.v = new Vector2(fv2x, fv2y);
        }
    }

    public Point2 getPosition(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

//    public Point2 getCords(boolean mode){
//        float m = mode ? 1.0f : 0.0f;
//        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT() / 2.0f));
//    }


    public float getDT() {
        return space.getDT();
    }

    @Override
    public Primitive getType() {
        return Primitive.SPHERE;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        int[] coords = new int[]{Tools.transformFloat(x0 - r), Tools.transformFloat(y0 - r), Tools.transformFloat(r * 2)};
        g.drawOval(coords[0], coords[1], coords[2], coords[2]);
//        g.fillOval(coords[0], coords[1], coords[2], coords[2]);
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + orientationVector.getX()),
                Tools.transformFloat(y0 + orientationVector.getY()));

        g.setColor(Color.green);
//
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + v.getX() * space.getDT()),
                Tools.transformFloat(y0 + v.getY() * space.getDT()));

        g.setColor(material.fillColor);
        g.fillOval(coords[0], coords[1], coords[2], coords[2]);
    }

}


