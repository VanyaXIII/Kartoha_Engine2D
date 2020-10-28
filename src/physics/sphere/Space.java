package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.LineEq;
import physics.geometry.Vector2D;

import java.util.ArrayList;

public class Space {
    public ArrayList<LineEq> lines;
    public ArrayList<ASS> things;
    public ArrayList<Drawable> drawables;
    private final double dt;
    private double time;
    private final double g;
    public final double height, width;
    private double correctEn;
    private double energy;
    private int amOfTh;

    public Space(double dt, double g, int width, int height) {
        this.dt = dt;
        this.g = g;
        this.time = 0;
        this.height = height;
        this.width = width;
        lines = new ArrayList<>();
        things = new ArrayList<>();
        drawables = new ArrayList<>();
        correctEn = 0.0;
        energy = 0.0;
        amOfTh = 0;
    }

    public void changeTime() {
        try {
            Thread.sleep((int) (100 * dt));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time += dt;
        long time1 = System.nanoTime();
        for (ASS thing : things) {
            thing.changeCoord();
        }
        ASS.collisionMode = !ASS.collisionMode;
        long time2 = System.nanoTime();
        countEn();
//        fixEnergy();
        System.out.println("calculating: "+(time2-time1));
    }

    private void countCorrectEn(ASS thing) {
        correctEn += thing.energy.count();
    }

    public void countEn() {
        energy = 1.0;
        for (ASS thing : things) {
            energy += thing.energy.count();
        }
    }

    public void fixEnergy() {
        double ratio = correctEn / energy;
        for (ASS thing : things) {
            thing.v.mul(Math.sqrt(ratio));
        }
        countEn();
    }


    public double getTime() {
        return time;
    }


    public void addLine(double x1, double y1, double x2, double y2) {
        LineEq line = new LineEq(x1, y1, x2, y2);
        lines.add(line);
        drawables.add(line);
    }

    public void addThing(Vector2D v, double x0, double y0, double r) {
        ASS thing = new ASS(this, v, x0, y0, r, this.g, this.dt);
        things.add(thing);
        countCorrectEn(thing);
        drawables.add(thing);
        amOfTh++;
    }

    public void addThing(Vector2D v, double x0, double y0, double r, Material material) {
        ASS thing = new ASS(this, v, x0, y0, r, this.g, this.dt, material);
        this.things.add(thing);
        this.countCorrectEn(thing);
        drawables.add(thing);
        amOfTh++;
    }

    public void printEnergy() {
        System.out.printf("Correct energy:\t%.5f\nReal energy:\t%.5f\n", correctEn, energy);
    }

    public void printThings() {
        System.out.printf("Total amount of things:\t%d\n", amOfTh);
    }
}
