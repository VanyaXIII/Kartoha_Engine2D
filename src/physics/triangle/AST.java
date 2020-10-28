package physics.triangle;

import physics.drawing.Drawable;
import physics.geometry.Point2;
import physics.geometry.TPolygon;
import physics.geometry.Vector2;
import physics.sphere.Material;
import physics.sphere.Space;
import physics.utils.Tools;

import java.awt.*;

public class AST extends Triangle implements Drawable {
    public Vector2 v;
    private final double g;
    private final Space space;
    public double w;

    public AST(Space space, Vector2 v, double w, double x0, double y0, double r) {
        super(x0, y0, r);
        this.space = space;
        this.v = v;
        this.w = w;
        this.g = space.g;
    }

    public AST(Space space, Vector2 v, double w, double x0, double y0, double r, Material material) {
        super(x0, y0, r, material);
        this.space = space;
        this.v = v;
        this.w = w;
        this.g = space.g;
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

    private void movePoints(){
        System.out.println(point1);
        point2.x += v.getX();
        point3.x += v.getX();
        point1.x += v.getX();
        point2.y += v.getY();
        point3.y += v.getY();
        point1.y += v.getY();
        System.out.println(point2);

    }

    private void rotate(){
        Point2 centre = new Point2(x0, y0);
        point1.rotate(centre, w);
        point2.rotate(centre, w);
        point3.rotate(centre, w);

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        TPolygon polygon = new TPolygon(point1, point2, point3);
        g.fillPolygon(polygon);
        g.setColor(Color.WHITE);
        g.drawLine(Tools.transformDouble(x0),
                Tools.transformDouble(y0),
                Tools.transformDouble(x0 + v.getX()),
                Tools.transformDouble(y0 + v.getY()));
    }
}
