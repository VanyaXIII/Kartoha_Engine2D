package physics.sphere;

import physics.utils.Tools;

import java.awt.*;

public class Sphere2D implements Drawable {
    public double x0, y0;
    public double m;
    public double r;
    private Material material;
    public Vector2D turnIdentificator;
    boolean md = false;

    Sphere2D(double x0, double y0, double r) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        this.material = new Material(1000, Color.RED);
        this.m = (Math.PI * r * r / 2) * material.p;
        this.turnIdentificator = new Vector2D(0, this.r);
    }

    Sphere2D(double x0, double y0, double r, Material material) {
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        this.material = material;
        this.m = (Math.PI * r * r / 2) * material.p;
        this.turnIdentificator = new Vector2D(0, this.r);
    }

    public Color getColor() {
        return material.color;
    }

    @Override
    public void draw(Graphics g) {
        int[] coords = new int[]{(int) Math.round(x0 - (double) r), (int) Math.round(y0 - (double) r), (int) Math.round(r * 2)};
        g.drawOval(coords[0], coords[1], coords[2], coords[2]);
        g.drawLine(Tools.transformDouble(x0),
                Tools.transformDouble(y0),
                Tools.transformDouble(x0 + turnIdentificator.getX()),
                Tools.transformDouble(y0 + turnIdentificator.getY()));
    }

}
