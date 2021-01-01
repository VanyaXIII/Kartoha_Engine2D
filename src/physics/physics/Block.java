package physics.physics;

import physics.drawing.Drawable;
import physics.utils.Tools;

import java.awt.*;

public class Block implements Drawable {
    private final float x;
    private final float y;
    private final float w, h;
    private final Material material;

    public Block(float x, float y, float w, float h, Material material) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.material = material;
    }

    public Wall[] getWalls() {
        Wall[] walls = new Wall[4];
        walls[0] = new Wall(x, y, x + w, y, material);
        walls[1] = new Wall(x, y + h, x + w, y + h, material);
        walls[2] = new Wall(x, y, x, y + h, material);
        walls[3] = new Wall(x+w, y, x + w, y + h, material);
        return walls;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        g.drawRect(Tools.transformFloat(x), Tools.transformFloat(y), Tools.transformFloat(w), Tools.transformFloat(h));
        g.setColor(material.fillColor);
        g.fillRect(Tools.transformFloat(x), Tools.transformFloat(y), Tools.transformFloat(w), Tools.transformFloat(h));
    }
}
