package physics.triangle;

import physics.drawing.Drawable;
import physics.geometry.Line;
import physics.geometry.Point2;
import physics.geometry.TPolygon;
import physics.geometry.Vector2;
import physics.sphere.Material;
import physics.sphere.Space;
import physics.utils.Tools;

import java.awt.*;

public class AST extends Triangle implements Drawable {
    public Vector2 v;
    private final float g;
    private final Space space;
    public float w;



    public AST(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
        super(x0, y0, r, material);
        this.space = space;
        this.v = v;
        this.w = w;
        this.g = 0;
    }

    public void changeCoord() {
        changeSpeed();
        rotate();
        movePoints();
        x0 += v.getX();
        y0 += v.getY();
    }

    private void changeSpeed() {
        v.addY(g);
    }

    private void movePoints() {
        point2.x += v.getX();
        point3.x += v.getX();
        point1.x += v.getX();
        point2.y += v.getY();
        point3.y += v.getY();
        point1.y += v.getY();

    }

    private void rotate() {
        Point2 centre = new Point2(x0, y0);
        point1.rotate(centre, w);
        point2.rotate(centre, w);
        point3.rotate(centre, w);

    }


    public Line[] getLines(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        Point2 centre = new Point2(x0 + v.getX() * m, y0 + v.getY()*m);
        Point2 newPoint1 = new Point2(point1.x + m * v.getX(), point1.y + m * v.getY());
        Point2 newPoint2 = new Point2(point2.x + m * v.getX(), point2.y + m * v.getY());
        Point2 newPoint3 = new Point2(point3.x + m * v.getX(), point3.y + m * v.getY());
        newPoint1.rotate(centre, w*m);
        newPoint2.rotate(centre, w*m);
        newPoint3.rotate(centre, w*m);
        return new Line[] {new Line(newPoint1, newPoint2),
                new Line(newPoint3, newPoint2),
                new Line(newPoint1, newPoint3)};
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        TPolygon polygon = new TPolygon(point1, point2, point3);
        g.drawPolygon(polygon);
//        g.fillPolygon(polygon);
        g.setColor(Color.WHITE);
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + v.getX()),
                Tools.transformFloat(y0 + v.getY()));
    }
}
