package physics.ui;

import physics.geometry.Vector2;
import physics.physics.Material;
import physics.physics.Space;

import java.awt.*;
import java.awt.event.*;

public class Scene {

    private final Space space;
    private final Frame frame;
    private final Controller controller;


    public Scene(Space space, Color bgColor, int width, int height) {
        this.space = space;
        frame = new Frame(this, bgColor, width, height);
        controller = new Controller(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1)
                    space.addPolygon(new Vector2(0, 0), 0f, e.getX(), e.getY(), 10, 180, Material.Steel);
                else if (e.getButton() == 3)
                    space.addPolygon(new Vector2(150, 0), 0f, e.getX(), e.getY(), 10, 180, Material.Steel);
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
                frame.addMouseListener(controller.getMouseListener());
                frame.addKeyListener(controller.getKeyListener());
                frame.addMouseMotionListener(controller.getMouseMotionListener());
        }


        public Space getSpace () {
            return space;
        }

//    public void addButton(Clickable clickHandler, Color color, Point2 lowerLeftCorner, float width, float height, String text){
//        buttons.add(new Button(clickHandler, color, lowerLeftCorner, width, height, text));
//    }

        public void update () {
            space.changeTime();
            frame.repaint();
        }
    }
