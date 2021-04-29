package Kartoha_Engine2D.physics;

import Kartoha_Engine2D.drawing.Drawable;
import Kartoha_Engine2D.utils.JsonAble;
import Kartoha_Engine2D.utils.Tools;

import java.awt.*;

public final class Block implements Drawable, JsonAble {

    private final float x;
    private final float y;
    private int z;
    private final float w, h;
    private final Material material;
    private transient Space space;

    {
        z = 0;
    }

    public Block(float x, float y, float w, float h, Material material, Space space) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.material = material;
        this.space = space;
    }

    public Wall[] getWalls() {
        Wall[] walls = new Wall[4];
        walls[0] = new Wall(x, y, x + w, y, material, space);
        walls[1] = new Wall(x, y + h, x + w, y + h, material, space);
        walls[2] = new Wall(x, y, x, y + h, material, space);
        walls[3] = new Wall(x+w, y, x + w, y + h, material, space);
        walls[0].setZ(z);
        walls[1].setZ(z);
        walls[2].setZ(z);
        walls[3].setZ(z);
        return walls;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        g.drawRect(Tools.transformFloat(x - space.getCamera().getXMovement()), Tools.transformFloat(y - space.getCamera().getYMovement()), Tools.transformFloat(w), Tools.transformFloat(h));
        g.setColor(material.fillColor);
        g.fillRect(Tools.transformFloat(x - space.getCamera().getXMovement()), Tools.transformFloat(y- space.getCamera().getYMovement()), Tools.transformFloat(w), Tools.transformFloat(h));
    }

    @Override
    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getH() {
        return h;
    }

    public float getW() {
        return w;
    }

    public Material getMaterial() {
        return material;
    }

    public void setSpace(Space space) {
        if (this.space == null) {
            this.space = space;
        }
    }
}
