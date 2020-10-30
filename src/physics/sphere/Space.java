package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.LineEq;
import physics.geometry.Vector2;
import physics.triangle.AST;
import physics.triangle.Triangle;
import physics.utils.Tools;
import physics.utils.threads.SphereThread;
import physics.utils.threads.TriangleThread;

import java.util.ArrayList;

//TODO бить пространство на части
//TODO слушатель столкновений и всего такого, не забыть про wait()
public class Space {
    public ArrayList<LineEq> lines, tlines;
    public ArrayList<ASS> spheres;
    public ArrayList<Drawable> drawables;
    public ArrayList<AST> triangles;
    public final double dt;
    private double time;
    public final double g;
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
        tlines = new ArrayList<>();
        spheres = new ArrayList<>();
        triangles = new ArrayList<>();
        drawables = new ArrayList<>();
        correctEn = 0.0;
        energy = 0.0;
        amOfTh = 0;
    }

    public void changeTime(){
        long time1 = System.nanoTime();
        SphereThread sthread = new SphereThread(this);
        TriangleThread tthread = new TriangleThread(this);
        sthread.start();
        tthread.start();
        ASS.collisionMode = !ASS.collisionMode;
        try {
            sthread.join();
            tthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double cTime = (System.nanoTime() - time1) / 1000000.0;
        System.out.println("Counting: "+cTime);
        countEn();
//        fixEnergy();
        double sleepTime = 0;
        if ( dt * 1000.0 - cTime > 0) {
            sleepTime = dt * 1000.0 - cTime;
        }
        System.out.println("Sleeping: "+sleepTime);
        try {
            Thread.sleep(Tools.transformDouble(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time += dt;
    }

    private void countCorrectEn(ASS thing) {
        correctEn += thing.energy.count();
    }

    public void countEn() {
        energy = 1.0;
        for (ASS thing : spheres) {
            energy += thing.energy.count();
        }
    }

    public void fixEnergy() {
        double ratio = correctEn / energy;
        for (ASS thing : spheres) {
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

    public void addThing(Vector2 v, double x0, double y0, double r) {
        ASS thing = new ASS(this, v, x0, y0, r);
        spheres.add(thing);
        countCorrectEn(thing);
        drawables.add(thing);
        amOfTh++;
    }

    public void addThing(Vector2 v, double x0, double y0, double r, Material material) {
        ASS sphere = new ASS(this, v, x0, y0, r, material);
        spheres.add(sphere);
        countCorrectEn(sphere);
        drawables.add(sphere);
        amOfTh++;
    }

    public void addTriangle(Vector2 v, double w, double x0, double y0, double r, Material material) {
        AST triangle = new AST(this, v, w, x0, y0, r, material);
        triangles.add(triangle);
        drawables.add(triangle);
    }

    public void addTriangle(Vector2 v, double w, double x0, double y0, double r) {
        AST triangle = new AST(this, v, w, x0, y0, r);
        triangles.add(triangle);
        drawables.add(triangle);
    }

    public void fillTLines(){
        for(AST triangle : triangles){
            triangle.returnLines(tlines);
        }
    }
    public void printEnergy() {
        System.out.printf("Correct energy:\t%.5f\nReal energy:\t%.5f\n", correctEn, energy);
    }

    public void printThings() {
        System.out.printf("Total amount of things:\t%d\n", amOfTh);
    }
}
