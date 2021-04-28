package Kartoha_Engine2D.sphere;

import Kartoha_Engine2D.drawing.Drawable;

import java.awt.*;

public abstract class Sphere2 implements Drawable {

    transient public float x0, y0;
    final transient private float r;

    Sphere2(float x0, float y0, float r) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
    }



    @Override
    public void draw(Graphics g) {
        int[] coords = new int[]{(int) Math.round(x0 - (double) r), (int) Math.round(y0 - (double) r), Math.round(r * 2)};
        g.drawOval(coords[0], coords[1], coords[2], coords[2]);
    }

}
