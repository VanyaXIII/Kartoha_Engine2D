package physics.sphere;

import physics.drawing.Drawable;
import physics.drawing.Primitive;
import physics.geometry.*;
import physics.physics.Energy;
import physics.physics.Material;
import physics.physics.Space;
import physics.physics.Wall;
import physics.utils.Tools;
import physics.utils.threads.SphereThread;

import java.awt.*;
import java.util.ArrayList;
//TODO сначала у одного шарик меняется скорость, а потом уже запускается расчет для другого, с учетом изменения скорости первого
//TODO надо все разюить по методам, чтобы сначала у всех менялась скорость
//TODO сделать просчет траектории
//TODO доделать коллизию, чтобы учитывались прямые

public class ASS extends Sphere2D implements Drawable, Intersectional {
    public Vector2 v;
    public double w;
    public Energy energy;
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
        this.space = space;
        this.v = v;
        this.w = w;
        this.energy = new Energy(this, space.getG(), space);
    }

    public void changeCord() {
        changeSpeed();
        rotate();
        x0 += v.getX() * space.getDT();
        y0 += (v.getY() + v.getY() - space.getG()*space.getDT()) * space.getDT() / 2.0f;
        energy.update();

    }

    private void changeSpeed() {
        flag = true;
        processSceneCollision();
        processCollision();
        energy.update();
        v.addY(space.getG()*space.getDT());
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
        orientationVector.rotate(w);
    }

    private void processCollision() {
        for (ASS thing : space.countableSpheres) {
            if (new Pair<ASS, ASS>( this, thing, true).isIntersected()) {
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
            if (new Pair<ASS, Wall>(this, wall, true).isIntersected()){
                processWallCollision(wall);
                flag = false;
            }
        }
//        for (AST triangle : space.triangles) {
//            synchronized (triangle) {
//                for (Line line : triangle.getLines(true)) {
//                    if (checkLineIntersection(line, true)) {
//                        processWallCollision(line);
//                        flag = false;
//                    }
//                }
//            }
//        }
    }

    private void pullSpheres(SphereIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point2 nCords = intersection.centralLine.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }

    private void dragSphereToWall(Wall wall) {
        float x = getCords(true)[0];
        float y = getCords(true)[1];
        float d = wall.calcDistance(x0, y0);
//        Point2 intersectPoint = wall.findIntPoint(new Line(x0, y0, x, y));
//        float len = (float) Math.sqrt((intersectPoint.x - x0) * (intersectPoint.x - x0) + (intersectPoint.y - y0) * (intersectPoint.y - y0));
//        Point2 nCords = v.movePoint(new Point2(x0, y0), len - len * r / d);
//        x0 = nCords.x;
//        y0 = nCords.y;
    }


    private void processWallCollision(Wall wall) {
        dragSphereToWall(wall);
        Vector2 axisX = new Vector2(wall);
        Vector2 axisY = axisX.createNormal();
        float fr = Tools.countAverage(material.coefOfFriction, wall.material.coefOfFriction);
        float v1x = v.countProjectionOn(axisX);
        float v1y = v.countProjectionOn(axisY);
//        float v2x = v1x - Math.abs(2.0f * fr * v1y);
//        w += Math.abs((4.0f * fr * v1y * -1.0f * Math.signum(v1x)) / (m * r * r));
//        System.out.println(w);
        Vector2 fv1x = axisX.createByFloat(v1x);
        Vector2 fv1y = axisY.createByFloat(-v1y);
        v = new Vector2(fv1x, fv1y);
        float red = Tools.countAverage(material.coefOfReduction, wall.material.coefOfReduction);
        v.mul(red);
        energy.update();
    }

    //TODO зафигачить перемещение при столкновении до положения столкновения
    private void processSphereCollision(ASS thing) {
        Vector2 axisX = new Vector2(this.getCords(true)[0] - thing.getCords(true)[0],
                this.getCords(true)[1] - thing.getCords(true)[1]);
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

    public float[] getCords(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new float[]{x0 + m * v.getX() * space.getDT(), y0 + m*((v.getY() + v.getY() - space.getG()*space.getDT()) * space.getDT() / 2.0f)};
    }


    public float getDT(){
        return space.getDT();
    }

    @Override
    public Primitive getType() {
        return Primitive.SPHERE;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
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
                Tools.transformFloat(y0 + v.getY()*space.getDT()));


    }

}


