package billiard_example;

import KartohaEngine2D.drawing.Drawable;
import KartohaEngine2D.geometry.Line;
import KartohaEngine2D.geometry.Point2;
import KartohaEngine2D.geometry.Vector2;
import KartohaEngine2D.physics.Material;
import KartohaEngine2D.polygons.PhysicalPolygon;
import KartohaEngine2D.ui.ObjectController;
import KartohaEngine2D.ui.Scene;
import KartohaEngine2D.utils.Tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Cue {
    private final float width;
    private final float height;
    private final PhysicalPolygon polygon;
    private final BufferedImage sprite;
    private final Scene scene;


    public Cue(BufferedImage sprite, Scene scene, Point2 centrePoint, float width, float height) {
        this.sprite = sprite;
        this.width = width;
        this.height = height;
        this.scene = scene;

        ArrayList<Point2> points = new ArrayList<>();

        {

            Point2 point1 = new Point2(centrePoint.x - width / 2, centrePoint.y - height / 2),
                    point2 = new Point2(centrePoint.x + width / 2, centrePoint.y - height / 2),
                    point3 = new Point2(centrePoint.x + width / 2, centrePoint.y + height / 2),
                    point4 = new Point2(centrePoint.x - width / 2, centrePoint.y + height / 2);
            points.add(point1);
            points.add(point2);
            points.add(point3);
            points.add(point4);
        }


        PhysicalPolygon polygon = new PhysicalPolygon(scene.getSpace(), new Vector2(0, 0), 0, centrePoint.x, centrePoint.y, points, Material.Gold);
        this.polygon = polygon;
        scene.getSpace().getPolygons().add(polygon);
        initDrawing();


        ObjectController controller = new ObjectController(polygon, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    polygon.setCords(new Point2(-1000, -1000));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        }, new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    polygon.setCords(new Point2(e.getX(), e.getY()));
                }
                if (e.getButton() == 2) {
                    Vector2 v = new Vector2(polygon.getPositionOfCentre(false), new Point2(Tools.countAverage(polygon.getPoints(false).get(1).x, polygon.getPoints(false).get(2).x),
                            Tools.countAverage(polygon.getPoints(false).get(1).y, polygon.getPoints(false).get(2).y)));
                    v.setLength(300);
                    polygon.setV(v);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }, new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                polygon.setCords(new Point2(e.getX(), e.getY()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        }, e -> polygon.rotate((float) (Math.PI * e.getPreciseWheelRotation() / 25)));
        scene.addObjectController(controller);
    }


    public void initDrawing() {
        if (sprite != null) {
            Drawable drawable = g -> {
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform backup = g2d.getTransform();
                AffineTransform tx = AffineTransform.getRotateInstance(polygon.getRotateAngle(), polygon.x0, polygon.y0);
                g2d.setTransform(tx);
                Point2 sP = new Point2(polygon.getPoints().get(0).x, polygon.getPoints().get(0).y);
                sP.rotate(new Point2(polygon.x0, polygon.y0), -polygon.getRotateAngle());
                g2d.drawImage(sprite, (int) sP.x, (int) sP.y,
                        (int) width,
                        (int) height, null);
                g2d.setTransform(backup);
                Point2 point = new Point2(Tools.countAverage(polygon.getPoints(false).get(1).x, polygon.getPoints(false).get(2).x),
                                    Tools.countAverage(polygon.getPoints(false).get(1).y, polygon.getPoints(false).get(2).y));
                Vector2 vector = new Vector2(polygon.getPositionOfCentre(false), point);
                vector.setLength(1000);
                g2d.setColor(new Color(217, 217, 217, 89));
                new Line(polygon.getPositionOfCentre(false), vector).draw(g2d);
            };
            scene.getSpace().getDrawables().add(drawable);
        } else {
            scene.getSpace().getDrawables().add(polygon);
        }
    }

}
