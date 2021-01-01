package physics.sphere;

import physics.drawing.Drawable;
import physics.physics.Material;

import java.awt.*;

public abstract class Sphere2D implements Drawable {
    public float x0, y0;
    public float m;
    public float r;
    public Material material;
    boolean md = false;

    Sphere2D(float x0, float y0, float r, Material material) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        this.material = material;
        this.m = ((float)Math.PI * r * r / 2) * material.p;
    }

    public Color getColor() {
        return material.outlineColor;
    }


    @Override
    public void draw(Graphics g) {
        int[] coords = new int[]{(int) Math.round(x0 - (double) r), (int) Math.round(y0 - (double) r), Math.round(r * 2)};
        g.drawOval(coords[0], coords[1], coords[2], coords[2]);
    }

}
