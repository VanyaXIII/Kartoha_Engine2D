package billiard_example;

import KartohaEngine2D.drawing.Drawable;
import KartohaEngine2D.geometry.Point2;
import KartohaEngine2D.geometry.Vector2;
import KartohaEngine2D.polygons.PhysicalPolygon;
import KartohaEngine2D.sphere.PhysicalSphere;
import KartohaEngine2D.ui.Scene;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Pocket implements Drawable {

    public final float r;
    public final float x0, y0;

    public Pocket(Scene scene, float r, float x0, float y0){
        this.r = r;
        this.x0 = x0;
        this.y0 = y0;
        scene.getSpace().getExecutables().add(() -> {
            Set<PhysicalSphere> spheresToDelete = new HashSet<>();
            for (PhysicalSphere sphere : scene.getSpace().getSpheres())
                if (new Vector2(new Point2(x0, y0), sphere.getPosition(false)).length() <= r)
                    spheresToDelete.add(sphere);
            for (var sphere : spheresToDelete){
                synchronized (scene.getSpace().getSpheres()) {
                    scene.getSpace().getSpheres().remove(sphere);
                }
                synchronized (scene.getSpace().getDrawables()) {
                    scene.getSpace().getDrawables().remove(sphere);
                }
            }
        });
        scene.getSpace().getDrawables().add(this);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval((int) (x0 - r), (int) (y0 - r), (int) (2*r), (int) (2*r));
        g.setColor(new Color(0,0,0, 255));
        g.fillOval((int) (x0 - r), (int) (y0 - r), (int) (2*r), (int) (2*r));
    }
}
