package physics.physics;

import physics.drawing.Drawable;
import physics.drawing.Primitive;
import physics.geometry.Intersectional;
import physics.geometry.Line;
import physics.geometry.Point2;
import physics.geometry.Vector2;
import physics.physics.Material;
import physics.utils.Tools;

import java.awt.*;

public class Wall extends Line implements Drawable, Intersectional {
    public Material material;

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

    @Override
    public void draw(Graphics g){
        g.setColor(material.color);
        g.drawLine(Tools.transformFloat(x1), Tools.transformFloat(y1),
                Tools.transformFloat(x2), Tools.transformFloat(y2));
    }

    @Override
    public Primitive getType() {
        return Primitive.WALL;
    }
}