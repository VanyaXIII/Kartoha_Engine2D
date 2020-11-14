package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.Line;
import physics.geometry.Point2;
import physics.geometry.Vector2;
import physics.utils.Tools;

import java.awt.*;
//TODO сначала у одного шарик меняется скорость, а потом уже запускается расчет для другого, с учетом изменения скорости первого
//TODO надо все разюить по методам, чтобы сначала у всех менялась скорость
//TODO сделать просчет траектории
//TODO доделать коллизию, чтобы учитывались прямые

public class ASS extends Sphere2D implements Drawable {
    public Vector2 v;
    public float w;
    public Energy energy;
    public static boolean collisionMode = true;
    private final float g;
    private Vector2 orientation = new Vector2(0, r);
    private final Space space;
    private boolean flag = true;

    // TODO подумать над полем пространства
    
    ASS(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
        super(x0, y0, r, material);
        this.space = space;
        this.v = v;
        this.w = w;
        this.g = space.g;
        this.energy = new Energy(this, g, space);
    }

    public void changeCoord() {
        float vy1 = v.getY();
        changeSpeed();
        rotate();
        x0 += v.getX();
        y0 += (vy1 + v.getY()) / 2.0;
        energy.update();

    }

    private void changeSpeed() {
        flag = true;
        processSceneCollision();
        processCollision();
        energy.update();
        if (flag) {
            v.addY(g);
        }
        processCollision();
    }

    private void processSceneCollision() {
        if (((y0 - (r) + v.getY() > 0.0) && (x0 + r + v.getX()) > space.width) || (x0 + v.getX() - r < 0.0)) {
            v.makeOpX();
        }
        if ((x0 - r + v.getX() > 0.0) && (y0 - r + v.getY() < 0.0)) {
            v.makeOpY();
        }
        if ((x0 + v.getX() + r > 0.0) && (y0 + v.getY() + r > space.height)) {
            v.makeOpY();
        }
    }

    private void rotate(){
        orientation.rotate(w);
    }

    private void processCollision() {
        for (ASS thing : space.countableSpheres) {
            if (checkSphereIntersection(thing, true).isIntersected) {
                processSphereCollision(thing);
            }
            if (collisionMode) {
                if (checkSphereIntersection(thing, false).isIntersected) {
                    pullSpheres(checkSphereIntersection(thing, false));
                }
            }
        }

        space.countableSpheres.remove(this);

        for (Wall wall : space.walls) {
            if (checkLineIntersection(wall, true)) {
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
        float x = countCoords(true)[0];
        float y = countCoords(true)[1];
        float fy = y0;
        float vy = v.getY();
        float d = wall.calcDistance(x0, y0);
        Point2 intersectPoint = wall.findIntPoint(new Line(x0, y0, x, y));
        float len = (float) Math.sqrt((intersectPoint.x - x0) * (intersectPoint.x - x0) + (intersectPoint.y - y0) * (intersectPoint.y - y0));
        Point2 nCords = v.movePoint(new Point2(x0, y0), len - len * r / d);
        x0 = nCords.x;
        y0 = nCords.y;
    }


    private boolean checkLineIntersection(Line line, boolean mode) {
        float x = countCoords(mode)[0];
        float y = countCoords(mode)[1];
        float d = line.calcDistance(x, y);
        if (d <= r) {
            if (line.y1 == line.y2) {
                return x >= line.minX() && x <= line.maxX();
            } else {
                return y >= line.minY() && y <= line.maxY();
            }
        } else return line.doesIntersect(new Line(new Point2(x0, y0), v));
    }

    private void processWallCollision(Wall wall) {
        Vector2 axisX = new Vector2(wall);
        Vector2 axisY = axisX.createNormal();
        float v1x = v.countProjectionOn(axisX);
        float v1y = v.countProjectionOn(axisY);
        Vector2 fv1x = axisX.createByFloat(v1x);
        Vector2 fv1y = axisY.createByFloat(-v1y);
        v = new Vector2(fv1x, fv1y);
//        dragSphereToWall(wall);
        float red = Tools.countAverage(material.coefOfReduction, wall.material.coefOfReduction);
        v.mul(red);
        energy.update();
    }

    private SphereIntersection checkSphereIntersection(ASS thing, boolean mode) {
        float x1 = this.countCoords(mode)[0];
        float y1 = this.countCoords(mode)[1];
        float x2 = thing.countCoords(mode)[0];
        float y2 = thing.countCoords(mode)[1];
        Vector2 dvector = new Vector2(x1 - x2,
                y1 - y2);
        float distance = dvector.length();
        if (distance < this.r + thing.r) {
            if (this.equals(thing)) {
                return new SphereIntersection(false);
            } else {
                if (distance != 0)
                    return new SphereIntersection(true, dvector, this.r + thing.r - distance);
                else
                    return new SphereIntersection(true, dvector, 0);
            }
        }
        return new SphereIntersection(false);
    }

    //TODO зафигачить перемещение при столкновении до положения столкновения
    private void processSphereCollision(ASS thing) {
        Vector2 axisX = new Vector2(this.countCoords(true)[0] - thing.countCoords(true)[0],
                this.countCoords(true)[1] - thing.countCoords(true)[1]);
        if (axisX.length() != 0.0) {
            Vector2 axisY = axisX.createNormal();
            float ratio = this.m / thing.m;
            float v1x = this.v.countProjectionOn(axisX);
            float v2x = thing.v.countProjectionOn(axisX);
            float v1y = this.v.countProjectionOn(axisY);
            float v2y = thing.v.countProjectionOn(axisY);
            float u1x = ((ratio - 1) / (ratio + 1)) * v1x + (2 / (ratio + 1)) * v2x;
            float u2x = ((ratio * 2) / (ratio + 1)) * v1x + ((1 - ratio) / (ratio + 1)) * v2x;
            Vector2 fv1x = axisX.createByFloat(u1x);
            Vector2 fv2x = axisX.createByFloat(u2x);
            Vector2 fv1y = axisY.createByFloat(v1y);
            Vector2 fv2y = axisY.createByFloat(v2y);
            this.v = new Vector2(fv1x, fv1y);
            thing.v = new Vector2(fv2x, fv2y);
        }
    }

    public float[] countCoords(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new float[]{x0 + m * v.getX(), y0 + m * (v.getY() + v.getY() + g) / 2.0f};
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        int[] coords = new int[]{Tools.transformFloat(x0 - r), Tools.transformFloat(y0 - r), Tools.transformFloat(r * 2)};
        g.drawOval(coords[0], coords[1], coords[2], coords[2]);
//        g.fillOval(coords[0], coords[1], coords[2], coords[2]);
//        g.drawLine(Tools.transformDouble(x0),
//                Tools.transformDouble(y0),
//                Tools.transformDouble(x0 + v.getX()),
//                Tools.transformDouble(y0));
//
//        g.drawLine(Tools.transformDouble(x0),
//                Tools.transformDouble(y0),
//                Tools.transformDouble(x0),
//                Tools.transformDouble(y0+v.getY()));
//
//        g.drawLine(Tools.transformDouble(x0),
//                Tools.transformDouble(y0),
//                Tools.transformDouble(x0 + v.getX()),
//                Tools.transformDouble(y0 + v.getY()));

        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + orientation.getX()),
                Tools.transformFloat(y0 + orientation.getY()));
    }

}

class SphereIntersection {
    public boolean isIntersected;
    public Vector2 centralLine;
    private float value;

    SphereIntersection(boolean isIntersected, Vector2 cl, float value) {
        this.isIntersected = isIntersected;
        this.centralLine = cl;
        this.value = value;
    }

    SphereIntersection(boolean isIntersected) {
        this.isIntersected = isIntersected;
    }

    public float getValue() {
        return value;
    }
}
