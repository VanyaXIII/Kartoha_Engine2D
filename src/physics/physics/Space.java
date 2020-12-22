package physics.physics;

import physics.drawing.Drawable;
import physics.geometry.Vector2;
import physics.sphere.ASS;
import physics.triangle.AST;
import physics.utils.Tools;
import physics.utils.threads.SphereThread;
import physics.utils.threads.TriangleThread;

import java.util.ArrayList;

//TODO бить пространство на части
//TODO слушатель столкновений и всего такого, не забыть про wait()
public class Space {
    public ArrayList<Wall> walls;
    public ArrayList<ASS> spheres;
    public ArrayList<ASS> countableSpheres;
    public ArrayList<Drawable> drawables;
    public ArrayList<Block> blocks;
    public ArrayList<AST> triangles;
    public final double height, width;
    private final float DT;
    private final float G;
    private double time;
    private double correctEn;
    private double energy;
    private int amOfTh;
    private float fps = 0;

    {
        walls = new ArrayList<>();
        spheres = new ArrayList<>();
        triangles = new ArrayList<>();
        drawables = new ArrayList<>();
        blocks = new ArrayList<>();
    }

    public Space(float dt, float g, int width, int height) {
        this.DT = dt;
        this.G = g;
        this.time = 0;
        this.height = height;
        this.width = width;
        correctEn = 0.0;
        energy = 0.0;
        amOfTh = 0;
    }

    public void changeTime() {
        long time1 = System.nanoTime();
        countableSpheres = (ArrayList<ASS>) spheres.clone();
        SphereThread sthread = new SphereThread(this);
        TriangleThread tthread = new TriangleThread(this);
        sthread.start();
        tthread.start();
        try {
            sthread.join();
            tthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ASS.collisionMode = !ASS.collisionMode;
        float cTime = (float) ((System.nanoTime() - time1) / 1000000.0);
        float sleepTime = 0;
        if (DT * 1000.0 - cTime > 0) {
            sleepTime = DT * 1000.0f - cTime;
        }
        try {
            Thread.sleep(Tools.transformFloat(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time += DT;
        long time2 = System.nanoTime();
        fps = 1000f / ((time2 - time1) / 1000000f);
    }



    public void addWall(float x1, float y1, float x2, float y2, Material material) {
        Wall wall = new Wall(x1, y1, x2, y2, material);
        walls.add(wall);
        drawables.add(wall);
    }


    public void addWall(float x1, float y1, float x2, float y2) {
        Wall wall = new Wall(x1, y1, x2, y2, Material.Constantin);
        walls.add(wall);
        drawables.add(wall);
    }

    public void addBlock(float x, float y, float w, float h) {
        Block block = new Block(x, y, w, h, Material.Constantin);
        blocks.add(block);
        drawables.add(block);
        Wall[] blockWalls = block.getWalls();
        walls.add(blockWalls[0]);
        walls.add(blockWalls[1]);
        walls.add(blockWalls[2]);
        walls.add(blockWalls[3]);
    }

    public void addBlock(float x, float y, float w, float h, Material material) {
        Block block = new Block(x, y, w, h, material);
        blocks.add(new Block(x, y, w, h, material));
        drawables.add(block);
        Wall[] blockWalls = block.getWalls();
        walls.add(blockWalls[0]);
        walls.add(blockWalls[1]);
        walls.add(blockWalls[2]);
        walls.add(blockWalls[3]);
    }

    public void addSphere(Vector2 v, float w, float x0, float y0, float r) {
        ASS thing = new ASS(this, v, w, x0, y0, r, Material.Constantin);
        spheres.add(thing);
        drawables.add(thing);
        amOfTh++;
    }

    public void addSphere(Vector2 v, float w, float x0, float y0, float r, Material material) {
        ASS sphere = new ASS(this, v, w, x0, y0, r, material);
        spheres.add(sphere);
        drawables.add(sphere);
        amOfTh++;
    }

    public void addTriangle(Vector2 v, float w, float x0, float y0, float r, Material material) {
        AST triangle = new AST(this, v, w, x0, y0, r, material);
        triangles.add(triangle);
        drawables.add(triangle);
    }

    public void addTriangle(Vector2 v, float w, float x0, float y0, float r) {
        AST triangle = new AST(this, v, w, x0, y0, r, Material.Constantin);
        triangles.add(triangle);
        drawables.add(triangle);
    }

    public void printEnergy() {
        System.out.printf("Correct energy:\t%.5f\nReal energy:\t%.5f\n", correctEn, energy);
    }

    public void printThings() {
        System.out.printf("Total amount of things:\t%d\n", amOfTh);
    }

    public double getTime() {
        return time;
    }

    public float getFps() {
        return fps;
    }

    public float getDT() {
        return DT;
    }

    public float getG() {
        return G;
    }

}
