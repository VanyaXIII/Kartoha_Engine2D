package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.Line;
import physics.geometry.Point2;
import physics.geometry.Vector2;
import physics.triangle.AST;
import physics.utils.Tools;

import java.awt.*;
//TODO сначала у одного шарик меняется скорость, а потом уже запускается расчет для другого, с учетом изменения скорости первого
//TODO надо все разюить по методам, чтобы сначала у всех менялась скорость
//TODO сделать просчет траектории
//TODO доделать коллизию, чтобы учитывались прямые

public class ASS extends Sphere2D implements Drawable {
    public Vector2 v;
    public Energy energy;
    public static boolean collisionMode = true;
    private final double g;
    private final Space space;
    private boolean flag = true;

    // TODO подумать над полем пространства
    ASS(Space space, Vector2 v, double x0, double y0, double r) {
        super(x0, y0, r);
        this.space = space;
        this.v = v;
        this.g = space.g;
        this.energy = new Energy(this, g, space);
    }

    ASS(Space space, Vector2 v, double x0, double y0, double r, Material material) {
        super(x0, y0, r, material);
        this.space = space;
        this.v = v;
        this.g = space.g;
        this.energy = new Energy(this, g, space);
    }

    public void changeCoord() {
        double vy1 = v.getY();
        changeSpeed();
        x0 += v.getX();
        y0 += (vy1 + v.getY()) / 2.0;
        energy.update();

    }

    private void changeSpeed() {
        flag = true;
        processLineCollision();
        processCollision();
        energy.update();
        if (flag) {
            v.addY(g);
        }
    }

    private void processLineCollision() {
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

    private void processCollision() {
        for (ASS thing : space.countableSpheres) {
            if (checkSphereIntersection(thing, true).isIntersected) {
                processSphereCollision(thing);
            }
            if (collisionMode) {
                if (checkSphereIntersection(thing, false).isIntersected) {
                    pullSpeheres(checkSphereIntersection(thing, false));
                }
            }
        }
        space.countableSpheres.remove(this);
        for (Line line : space.lines) {
            if (checkLineIntersection(line, true)) {
                processLineCollision(line);
                flag = false;
            }
        }
        for (AST triangle : space.triangles) {
            synchronized (triangle) {
                for (Line line : triangle.getLines(true)) {
                    if (checkLineIntersection(line, true)) {
                        processLineCollision(line);
                        flag = false;
                    }
                }
            }
        }
    }

    private void pullSpeheres(SphereIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point2 nCoords = intersection.centralLine.movePoint(new Point2(this.x0, this.y0), intersection.getValue());
            this.x0 = nCoords.x;
            this.y0 = nCoords.y;
        }
    }

    private void dragSphereToLine(Line line) {
        double x = countCoords(true)[0];
        double y = countCoords(true)[1];
        double fy = y0;
        double vy = v.getY();
        double d = line.calcDistance(x0, y0);
        Point2 intersectPoint = line.findIntPoint(new Line(x0, y0, x, y));
        double len = Math.sqrt((intersectPoint.x - x0) * (intersectPoint.x - x0) + (intersectPoint.y - y0) * (intersectPoint.y - y0));
        Point2 nCords = v.movePoint(new Point2(x0, y0), len - len * r / d);
        x0 = nCords.x;
        y0 = nCords.y;
    }

    private boolean lineCollision() {
        for (Line line : space.lines) {
            checkLineIntersection(line, false);
        }
        return false;
    }

    private boolean checkLineIntersection(Line line, boolean mode) {
        double x = countCoords(mode)[0];
        double y = countCoords(mode)[1];
        double d = line.calcDistance(x, y);
        if (d <= r) {
            if (line.y1 == line.y2) {
                return x >= line.minX() && x <= line.maxX();
            } else {
                return y >= line.minY() && y <= line.maxY();
            }
        } else return line.doesIntersect(new Line(new Point2(x0, y0), v));
    }

    private void processLineCollision(Line line) {
        Vector2 axisX = new Vector2(line);
        Vector2 axisY = axisX.createNormal();
        double v1x = v.countProjectionOn(axisX);
        double v1y = v.countProjectionOn(axisY);
        Vector2 fv1x = axisX.createByDouble(v1x);
        Vector2 fv1y = axisY.createByDouble(-v1y);
        v = new Vector2(fv1x, fv1y);
        dragSphereToLine(line);
        energy.update();
        System.out.println(energy);
    }

    private SphereIntersection checkSphereIntersection(ASS thing, boolean mode) {
        double x1 = this.countCoords(mode)[0];
        double y1 = this.countCoords(mode)[1];
        double x2 = thing.countCoords(mode)[0];
        double y2 = thing.countCoords(mode)[1];
        Vector2 dvector = new Vector2(x1 - x2,
                y1 - y2);
        double distance = dvector.length();
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
            double ratio = this.m / thing.m;
            double v1x = this.v.countProjectionOn(axisX);
            double v2x = thing.v.countProjectionOn(axisX);
            double v1y = this.v.countProjectionOn(axisY);
            double v2y = thing.v.countProjectionOn(axisY);
            double u1x = ((ratio - 1) / (ratio + 1)) * v1x + (2 / (ratio + 1)) * v2x;
            double u2x = ((ratio * 2) / (ratio + 1)) * v1x + ((1 - ratio) / (ratio + 1)) * v2x;
            Vector2 fv1x = axisX.createByDouble(u1x);
            Vector2 fv2x = axisX.createByDouble(u2x);
            Vector2 fv1y = axisY.createByDouble(v1y);
            Vector2 fv2y = axisY.createByDouble(v2y);
            this.v = new Vector2(fv1x, fv1y);
            thing.v = new Vector2(fv2x, fv2y);
        }
    }

    public double[] countCoords(boolean mode) {
        double m = mode ? 1.0 : 0.0;
        return new double[]{x0 + m * v.getX(), y0 + m * (v.getY() + v.getY() + g) / 2.0};
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        int[] coords = new int[]{Tools.transformDouble(x0 - r), Tools.transformDouble(y0 - r), Tools.transformDouble(r * 2)};
        g.drawOval(coords[0], coords[1], coords[2], coords[2]);
        g.fillOval(coords[0], coords[1], coords[2], coords[2]);
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

//        g.drawLine(Tools.transformDouble(x0),
//                Tools.transformDouble(y0),
//                Tools.transformDouble(x0 + turnId.getX()),
//                Tools.transformDouble(y0 + turnId.getY()));
    }

}

class SphereIntersection {
    public boolean isIntersected;
    public Vector2 centralLine;
    private double value;

    SphereIntersection(boolean isIntersected, Vector2 cl, double value) {
        this.isIntersected = isIntersected;
        this.centralLine = cl;
        this.value = value;
    }

    SphereIntersection(boolean isIntersected) {
        this.isIntersected = isIntersected;
    }

    public double getValue() {
        return value;
    }
}


