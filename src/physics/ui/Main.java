package physics.ui;


import physics.geometry.Point2;
import physics.geometry.Vector2;
import physics.physics.Material;
import physics.physics.Space;
import physics.polygons.PhysicalPolygon;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

final class Main {
    public static void main(String[] args) throws InterruptedException {
        Scene scene = new Scene(new Space(0.0025f, 000f), new Color(9, 73, 26, 255), 1600, 1000);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1100, 500, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1130, 520, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1130, 480, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 500, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 460, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 540, 20, Material.Constantin);
        scene.getSpace().addBlock(-100, -100, 2000, 200, Material.Wood);
        scene.getSpace().addBlock(1500, -100, 200, 2000, Material.Wood);
        scene.getSpace().addBlock(-100, -100, 200, 2000, Material.Wood);
        Point2 point1 = new Point2(200,520),
                point2 = new Point2(200, 480),
                point3 = new Point2(600, 480),
                point4 = new Point2(600, 520);
        SceneController controller = new SceneController(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Space space = scene.getSpace();
                if (e.getButton() == 1)
                    space.addPolygon(new Vector2(0, 0), 0f, e.getX(), e.getY(), 10, 180, Material.Constantin);
                else if (e.getButton() == 3)
                    space.addSphere(new Vector2(150, 0), 0f, e.getX(), e.getY(), 70);
                else space.deleteDynamicObjects();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        }, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Space space = scene.getSpace();
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    space.deleteDynamicObjects();
                if (e.getKeyCode() == KeyEvent.VK_G)
                    space.setG(300);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        }, new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
//                space.addSphere(new Vector2(00, 0), 0, e.getX(), e.getY(), 40, Material.Constantin);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        ArrayList<Point2> points = new ArrayList<>();
        points.add(point1);
        points.add(point4);
        points.add(point3);
        points.add(point2);
        PhysicalPolygon a = new PhysicalPolygon(scene.getSpace(), new Vector2(0,0), 0f,400,500, points, Material.Gold);
        a.rotate((float) (Math.PI/ 4));
        scene.addObjectController(new ObjectController(a, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    a.setCords(new Point2(-1000, -1000));
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
                    a.setCords(new Point2(e.getX(), e.getY()));
                }
                if (e.getButton() == 2) {
                    a.setV(new Vector2(400, 00));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }, null, new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                a.rotate((float) (Math.PI  * e.getPreciseWheelRotation() / 25));
            }
        }));
        scene.getSpace().getPolygons().add(a);
        scene.getSpace().getDrawables().add(a);
        scene.getSpace().addBlock(-1000, 920, 4000, 200, Material.Wood);
        while (2 + 2 == 4) scene.update();
    }
}

