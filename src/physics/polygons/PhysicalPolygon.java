package physics.polygons;

import physics.drawing.ArbitraryFigure;
import physics.drawing.Drawable;
import physics.geometry.*;
import physics.physics.Material;
import physics.physics.Space;
import physics.utils.Tools;

import java.awt.*;
import java.util.ArrayList;

public class PhysicalPolygon extends Polygon implements Drawable, Collisional, Intersectional {

    private Vector2 v;
    private final Space space;
    private float w;
    private final float m;
    private final float J;
    private final Material material;



    public PhysicalPolygon(Space space, Vector2 v, float w, float x0, float y0, ArrayList<Point2> points, Material material) {
        super(x0, y0, points);
        this.space = space;
        this.v = v;
        this.w = w;
        this.material = material;
        PolygonCreator polygonCreator = new PolygonCreator(points);
        this.m = polygonCreator.getSquare() * material.p;
        this.J = polygonCreator.getJDivDensity() * material.p;
    }

    public synchronized void update() {
        updateSpeed();
        rotate();
        updatePoints();
        x0 += v.getX() * space.getDT();
        y0 += ((v.getY() + v.getY() - space.getG()*space.getDT())/2.0f) * space.getDT();
    }

    private void updateSpeed() {
        v.addY(space.getG()*space.getDT());
    }

    private void updatePoints() {
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

    public Point2 getPositionOfCentre(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    public ArrayList<Line> getLines(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        ArrayList<Point2> newPoints = getPoints(mode);

        ArrayList<Line> lines = new ArrayList<>();

        for (int i = 0; i<newPoints.size(); i++){
            if (i+1 < newPoints.size()) lines.add(new Line(newPoints.get(i), newPoints.get(i+1)));
            else lines.add(new Line(newPoints.get(0), newPoints.get(i)));
        }
        return lines;
    }

    public ArrayList<Point2> getPoints(boolean mode){
        float m = mode ? 1.0f : 0.0f;
        Point2 centre = new Point2(x0 + v.getX() * m*space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));

        ArrayList<Point2> newPoints = clonePoints();
        for (Point2 point : newPoints) {
            point.x += m * v.getX() * space.getDT();
            point.y += m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT()) / 2.0f;
            point.rotate(centre, w * space.getDT() * m);
        }
        
        return newPoints;
    }

    public synchronized void pullFromLine(PolygonToLineIntersection intersection) {
        if (intersection.getValue() != 0){
            Vector2 movementVector = new Vector2(intersection.getPointOfPolygon(), intersection.getCollisionPoint());
            if (movementVector.length() != 0f)
                move(movementVector, intersection.getValue());
        }
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

    public float getJ() {
        assert J == 0 : "J is null";
        return J;
    }

    public float getW() {
        return w;
    }

    public float getM() {
        assert m == 0 : "Mass is null";
        return m;
    }

    public Vector2 getV() {
        return v;
    }

    public void setV(Vector2 v) {
        this.v = v;
    }

    public void setW(float w) {
        this.w = w;
    }

    public Material getMaterial() {
        return material;
    }
}
