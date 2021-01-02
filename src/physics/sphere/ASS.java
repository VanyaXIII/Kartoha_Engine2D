package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.Primitive;
import physics.geometry.*;
import physics.physics.Material;
import physics.physics.Space;
import physics.utils.Tools;

import java.awt.*;
//TODO сначала у одного шарик меняется скорость, а потом уже запускается расчет для другого, с учетом изменения скорости первого
//TODO надо все разюить по методам, чтобы сначала у всех менялась скорость
//TODO сделать просчет траектории
//TODO доделать коллизию, чтобы учитывались прямые

public class ASS extends Sphere2D implements Drawable, Collisional {
    private float x0, y0;
    public Vector2 v;
    public float w;
    private final Vector2 orientationVector;
    private final Space space;

    {
        orientationVector = new Vector2(0, r);
    }

    public ASS(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
        super(x0, y0, r, material);
        this.x0 = x0;
        this.y0 = y0;
        this.space = space;
        this.v = v;
        this.w = w;
    }

    public synchronized void update() {
        changeSpeed();
        rotate();
        x0 += v.getX() * space.getDT();
        y0 += (v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT() / 2.0f;

    }

    private synchronized void changeSpeed() {
        processSceneCollision();
        v.addY(space.getG() * space.getDT());
        processSceneCollision();
    }

    private void processSceneCollision() {
        if (((y0 - (r) + v.getY() * space.getDT() > 0.0) && (x0 + r + v.getX() * space.getDT()) > space.width) || (x0 + v.getX() * space.getDT() - r < 0.0)) {
            v.makeOpX();
        }
        if ((x0 - r + v.getX() * space.getDT() > 0.0) && (y0 - r + v.getY() * space.getDT() < 0.0)) {
            v.makeOpY();
        }
        if ((x0 + v.getX() * space.getDT() + r > 0.0) && (y0 + v.getY() * space.getDT() + r > space.height)) {
            v.makeOpY();
        }
    }

    private synchronized void rotate() {
        orientationVector.rotate(w * space.getDT());
    }


    public synchronized void pullSpheres(SphereIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point2 nCords = intersection.centralLine.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }

    public synchronized void pullSphereFromLine(SphereToLineIntersection intersection) {
        if (intersection.getValue() != 0){
            System.out.println(11111);
            Vector2 movementVector = new Vector2(intersection.getCollisionalPoint(), getPosition(false));
            Point2 nCords = movementVector.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }


    public synchronized Point2 getPosition(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    @Override
    public Primitive getType() {
        return Primitive.SPHERE;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        int[] cords = new int[]{Tools.transformFloat(x0 - r), Tools.transformFloat(y0 - r), Tools.transformFloat(r * 2)};
        g.drawOval(cords[0], cords[1], cords[2], cords[2]);
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + orientationVector.getX()),
                Tools.transformFloat(y0 + orientationVector.getY()));

        g.setColor(Color.green);
//
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + v.getX() * space.getDT()),
                Tools.transformFloat(y0 + v.getY() * space.getDT()));

        g.setColor(material.fillColor);
        g.fillOval(cords[0], cords[1], cords[2], cords[2]);
    }

}


