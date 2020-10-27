package physics.sphere;

import physics.utils.Tools;

import java.awt.*;

public class AST extends Sphere2D implements Drawable {
    public Vector2D v;
    public Energy energy;
    private final double time, g;
    private Space space;

    AST(Space space, Vector2D v, double x0, double y0, double r, double g, double dt) {
        super(x0, y0, r);
        this.space = space;
        this.time = dt;
        this.v = v;
        this.g = g;
        this.energy = new Energy(this, g, space);
    }

    AST(Space space, Vector2D v, double x0, double y0, double r, double g, double dt, Material material) {
        super(x0, y0, r, material);
        this.space = space;
        this.time = dt;
        this.v = v;
        this.g = g;
        this.energy = new Energy(this, g, space);
    }

    public void changeCoord() {
        changeSpeed();
        sphereCollision();
        x0 += v.getX() * time;
        y0 += v.getY() * time;
//        sphereCollision();
    }

    private void changeSpeed() {
        processClash();
        reflectSpeed();
        v.addY(g * time);
        energy.change();
    }

    private void reflectSpeed() {
        if (((y0 - (r) + v.getY() * time > 0.0) && (x0 + r + v.getX() * time) > space.width) || (x0 + v.getX() * time - r < 0.0)) {
            v.makeOpX();
        }
        if ((x0 - r + v.getX() * time > 0.0) && (y0 - r + v.getX() * time < 0.0)) {
            v.makeOpY();
        }
        if ((x0 + v.getX() * time + r > 0.0) && (y0 + v.getY() * time + r > space.height)) {
            v.makeOpY();
        }
    }

    private void processClash() {
        for (AST thing : space.things) {
            if (checkSphereIntersection(thing, true).isIntersected) reflectSpeed(thing);
        }
        for (LineEq line : space.lines) {
            if (checkLineIntersection(line, true)) reflectSpeed(line);
        }
    }

    private void sphereCollision() {
        for (AST thing : space.things) {
            Intersection intersection = checkSphereIntersection(thing, false);
            if (intersection.isIntersected) {
                Point2D nCoords1 = intersection.centralLine.movePoint(new Point2D(this.x0, this.y0), intersection.getValue());
                Point2D nCoords2 = intersection.centralLine.createOpVect().movePoint(new Point2D(thing.x0, thing.y0), intersection.getValue());
                this.x0 = nCoords1.x;
                this.y0 = nCoords1.y;
                thing.x0 = nCoords2.x;
                thing.y0 = nCoords2.y;
            }
        }
//        for (LineEq line : space.lines) {
//            if (checkLineIntersection(line, false)) return true;
//        }
    }

    private boolean checkLineIntersection(LineEq line, boolean mode) {
        double x = countCoords(mode)[0];
        double y = countCoords(mode)[1];
        double d = line.calcDistance(x, y);
        if (d <= r) {
            if (line.y1 == line.y2) {
                if (x >= line.minX() && x <= line.maxX()) return true;
            } else {
                if (y >= line.minY() && y <= line.maxY()) return true;
            }
        } else return false;
        return false;
    }

    private void reflectSpeed(LineEq line) {
        Vector2D n = line.makeNormalVector();
        n.makeUnit();
        double dot = v.dot(n);
        n.mul(dot * 2.0);
        v.setX(v.getX() - n.getX());
        v.setY(v.getY() - n.getY());
    }


    private Intersection checkSphereIntersection(AST thing, boolean mode) {
        double x1 = this.countCoords(mode)[0];
        double y1 = this.countCoords(mode)[1];
        double x2 = thing.countCoords(mode)[0];
        double y2 = thing.countCoords(mode)[1];
        Vector2D dvector = new Vector2D(x1 - x2,
                y1 - y2);
        double distance = dvector.length();
        if (distance < this.r + thing.r) {
            if (this.equals(thing)) {
                return new Intersection(false);
            } else {
                return new Intersection(true, this, thing, dvector, this.r + thing.r - distance);
            }
        }
        return new Intersection(false);
    }

    private void reflectSpeed(AST thing) {
        Vector2D axisX = new Vector2D(this.countCoords(true)[0] - thing.countCoords(true)[0],
                this.countCoords(true)[1] - thing.countCoords(true)[1]);
        Vector2D axisY = axisX.createNormal();
        double ratio = this.m / thing.m;
        double v1x = this.v.countProjectionOn(axisX);
        double v2x = thing.v.countProjectionOn(axisX);
        double v1y = this.v.countProjectionOn(axisY);
        double v2y = thing.v.countProjectionOn(axisY);
        double u1x = ((ratio - 1) / (ratio + 1)) * v1x + (2 / (ratio + 1)) * v2x;
        double u2x = ((ratio * 2) / (ratio + 1)) * v1x + ((1 - ratio) / (ratio + 1)) * v2x;
        double u1y = v1y;
        double u2y = v2y;
        Vector2D fv1x = axisX.createByDouble(u1x);
        Vector2D fv2x = axisX.createByDouble(u2x);
        Vector2D fv1y = axisY.createByDouble(u1y);
        Vector2D fv2y = axisY.createByDouble(u2y);
        this.v = new Vector2D(fv1x, fv1y);
        thing.v = new Vector2D(fv2x, fv2y);
    }

    public double[] countCoords(boolean mode) {
        double m = 0.0;
        if (mode) m = 1.0;
        return new double[]{x0 + m * v.getX() * time, y0 + m * v.getY() * time};
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.getColor());
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
//                Tools.transformDouble(x0 + turnIdentificator.getX()),
//                Tools.transformDouble(y0 + turnIdentificator.getY()));
    }

}

class Intersection {
    public boolean isIntersected;
    public Vector2D centralLine;
    private double value;
    public AST thing1, thing2;

    Intersection(boolean isIntersected, AST thing1, AST thing2, Vector2D cl, double value) {
        this.isIntersected = isIntersected;
        this.centralLine = cl;
        this.value = value;
    }

    Intersection(boolean isIntersected) {
        this.isIntersected = isIntersected;
    }

    public double getValue() {
        return value;
    }
}