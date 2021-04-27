package engine.physics;

import engine.drawing.Drawable;
import engine.limiters.Collisional;
import engine.limiters.Intersectional;
import engine.utils.Tools;
import engine.geometry.Line;
import engine.geometry.Point2;
import engine.geometry.Vector2;

import java.awt.*;

public class Wall extends Line implements Drawable, Collisional, Intersectional {

    private Material material;

    public Wall(float x1, float y1, float x2, float y2, Material material) {
        super(x1, y1, x2, y2);
        this.material = material;
    }

    public Wall(Point2 point1, Point2 point2, Material material) {
        super(point1, point2);
        this.material = material;
    }

    public Wall(Point2 point, Vector2 vector, Material material) {
        super(point, vector);
        this.material = material;
    }

    public Wall(Line line, Material material){
        super(line.x1, line.y1, line.x2, line.y2);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public Line toLine(){
        return new Line(x1, y1, x2, y2);
    }

    @Override
    public void draw(Graphics g){
        g.setColor(material.outlineColor);
        g.drawLine(Tools.transformFloat(x1), Tools.transformFloat(y1),
                Tools.transformFloat(x2), Tools.transformFloat(y2));
    }

}
