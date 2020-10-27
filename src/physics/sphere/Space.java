package physics.sphere;

import java.util.ArrayList;

public class Space {
    public ArrayList<LineEq> lines;
    public ArrayList<AST> things;
    private final double dt;
    private double time;
    private final double g;
    public final double height, width;
    private double correctEn;
    private double energy;
    private int amOfTh;

    Space(double dt, double g, int width, int height) {
        this.dt = dt;
        this.g = g;
        this.time = 0;
        this.height = height;
        this.width = width;
        lines = new ArrayList<>();
        things = new ArrayList<>();
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
        for (AST thing : things) {
            thing.changeCoord();
        }
        AST.collisionMode = !AST.collisionMode;
        long time2 = System.nanoTime();
        countEn();
//        fixEnergy();
        System.out.println("calculating: "+(time2-time1));
    }

    private void countCorrectEn(AST thing) {
        correctEn += thing.energy.count();
    }

    public void countEn() {
        energy = 1.0;
        for (AST thing : things) {
            energy += thing.energy.count();
        }
    }

    public void fixEnergy() {
        double ratio = correctEn / energy;
        for (AST thing : things) {
            thing.v.mul(Math.sqrt(ratio));
        }
        countEn();
    }


    public double getTime() {
        return time;
    }


    public void addLine(double x1, double y1, double x2, double y2) {
        LineEq line = new LineEq(x1, y1, x2, y2);
        this.lines.add(line);
    }

    public void addThing(Vector2D v, double x0, double y0, double r) {
        AST thing = new AST(this, v, x0, y0, r, this.g, this.dt);
        this.things.add(thing);
        this.countCorrectEn(thing);
        amOfTh++;
    }

    public void addThing(Vector2D v, double x0, double y0, double r, Material material) {
        AST thing = new AST(this, v, x0, y0, r, this.g, this.dt, material);
        this.things.add(thing);
        this.countCorrectEn(thing);
        amOfTh++;
    }

    public void printEnergy() {
        System.out.printf("Correct energy:\t%.5f\nReal energy:\t%.5f\n", correctEn, energy);
    }

    public void printThings() {
        System.out.printf("Total amount of things:\t%d\n", amOfTh);
    }
}
