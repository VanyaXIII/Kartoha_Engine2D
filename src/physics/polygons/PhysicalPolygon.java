package physics.polygons;

import physics.drawing.ArbitraryFigure;
import physics.drawing.Drawable;
import physics.geometry.Primitive;
import physics.geometry.*;
import physics.physics.Material;
import physics.physics.Space;
import physics.utils.Tools;

import java.awt.*;
import java.util.ArrayList;
//TODO реализовать наследование от класса полигон (RigidBody etc), который бы хранил в себе основные методы полигонов
public class PhysicalPolygon extends Polygon implements Drawable, Collisional {
    public Vector2 v;
    private final Space space;
    public float w;



    public PhysicalPolygon(Space space, Vector2 v, float w, float x0, float y0, ArrayList<Point2> points, Material material) {
        super(x0, y0, points, material);
        this.space = space;
        this.v = v;
        this.w = w;
    }

    public void update() {
        changeSpeed();
        rotate();
        movePoints();
        x0 += v.getX() * space.getDT();
        y0 += ((v.getY() + v.getY() - space.getG()*space.getDT())/2.0f) * space.getDT();
    }

    private void changeSpeed() {
        v.addY(space.getG()*space.getDT());
    }

    private void movePoints() {
        for (Point2 point : getPoints()){
            point.x += v.getX()* space.getDT();
            point.y += ((v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT())/ 2.0f;
        }
    }

    private void rotate() {
        Point2 centre = new Point2(x0, y0);
        for (Point2 point : getPoints()){
            point.rotate(centre, w * space.getDT());
        }

    }

    public Point2 getPosition(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    public ArrayList<Line> getLines(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        Point2 centre = new Point2(x0 + v.getX() * m*space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
        ArrayList<Point2> newPoints = clonePoints();
        for (Point2 point : newPoints) {
            point.x += m * v.getX() * space.getDT();
            point.y += m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT()) / 2.0f;
            point.rotate(centre, w * space.getDT());
        }

        ArrayList<Line> lines = new ArrayList<>();

        for (int i = 0; i<newPoints.size(); i++){
            if (i+1 < newPoints.size()) lines.add(new Line(newPoints.get(i), newPoints.get(i+1)));
            else lines.add(new Line(newPoints.get(0), newPoints.get(i)));
        }
        return lines;
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        ArbitraryFigure arbitraryFigure = new ArbitraryFigure(getPoints());
        g.drawPolygon(arbitraryFigure.getPolygon());
        g.setColor(material.fillColor);
        g.fillPolygon(arbitraryFigure.getPolygon());
        g.setColor(Color.WHITE);
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + v.getX()*space.getDT()),
                Tools.transformFloat(y0 + v.getY()*space.getDT()));
    }

}
