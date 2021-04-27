package com.github.gurianov.engine.physics;

import com.github.gurianov.engine.drawing.Drawable;
import com.github.gurianov.engine.geometry.*;
import com.github.gurianov.engine.limiters.Collisional;
import com.github.gurianov.engine.limiters.Intersectional;
import com.github.gurianov.engine.utils.Tools;
import com.github.gurianov.engine.geometry.Line;
import com.github.gurianov.engine.geometry.Point2;
import com.github.gurianov.engine.geometry.Vector2;

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
