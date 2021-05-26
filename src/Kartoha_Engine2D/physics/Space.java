package Kartoha_Engine2D.physics;

import Kartoha_Engine2D.drawing.Drawable;
import Kartoha_Engine2D.drawing.camera.Camera;
import Kartoha_Engine2D.drawing.camera.Focusable;
import Kartoha_Engine2D.drawing.camera.Resolution;
import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.geometry.PolygonCreator;
import Kartoha_Engine2D.geometry.Vector2;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.polygons.PhysicalPolygon;
import Kartoha_Engine2D.utils.Executable;
import Kartoha_Engine2D.utils.Tools;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Space {

    private final ArrayList<Drawable> drawables;
    private final ArrayList<Executable> executables;
    private float fps = 0;
    private final ArrayList<Wall> walls;
    transient private final ArrayList<PhysicalSphere> spheres;
    transient private final ArrayList<Block> blocks;
    transient private final ArrayList<PhysicalPolygon> polygons;
    private final float DT;
    private float G;
    private final PhysicsHandler physicsHandler;
    private Camera camera;

    {
        camera = new Camera(new Point2(0f, 0f), null);
        executables = new ArrayList<>();
        walls = new ArrayList<>();
        spheres = new ArrayList<>();
        polygons = new ArrayList<>();
        drawables = new ArrayList<>();
        blocks = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
    }

    public Space(float dt, float g) {
        this.DT = dt;
        this.G = g;
    }

    public void initCamera(Resolution resolution){
        if (camera.getResolution() == null) this.camera = new Camera(camera.getMin(), resolution);
    }

    public synchronized void changeTime() {

        long time1 = System.nanoTime();

        try {
            physicsHandler.update();
            for (Executable executable : executables){
                executable.execute();
            }
            camera.centre();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        long time2 = System.nanoTime();
        fps = 1000f / ((time2 - time1) / 1000000f);


    }



    public void addWall(float x1, float y1, float x2, float y2, Material material) {
        Wall wall = new Wall(x1, y1, x2, y2, material, this);
        walls.add(wall);
        drawables.add(wall);
    }


    public void addWall(float x1, float y1, float x2, float y2) {
        Wall wall = new Wall(x1, y1, x2, y2, Material.CONSTANTIN, this);
        walls.add(wall);
        drawables.add(wall);
    }

    public void addBlock(float x, float y, float w, float h) {
        Block block = new Block(x, y, w, h, Material.CONSTANTIN, this);
        blocks.add(block);
        drawables.add(block);
        Wall[] blockWalls = block.getWalls();
        walls.add(blockWalls[0]);
        walls.add(blockWalls[1]);
        walls.add(blockWalls[2]);
        walls.add(blockWalls[3]);
    }

    public void addBlock(float x, float y, float w, float h, Material material) {
        Block block = new Block(x, y, w, h, material, this);
        blocks.add(new Block(x, y, w, h, material, this));
        drawables.add(block);
        Wall[] blockWalls = block.getWalls();
        walls.add(blockWalls[0]);
        walls.add(blockWalls[1]);
        walls.add(blockWalls[2]);
        walls.add(blockWalls[3]);
    }

    public void addSphere(Vector2 v, float w, float x0, float y0, float r) {
        PhysicalSphere thing = new PhysicalSphere(this, v, w, x0, y0, r, Material.CONSTANTIN);
        spheres.add(thing);
        drawables.add(thing);
    }

    public void addSphere(Vector2 v, float w, float x0, float y0, float r, Material material) {
        PhysicalSphere sphere = new PhysicalSphere(this, v, w, x0, y0, r, material);
        synchronized (spheres) {
            spheres.add(sphere);
            drawables.add(sphere);
        }
    }

    public void addPolygon(Vector2 v, float w, float x0, float y0, int numOfPoints, float r, Material material) {
        PolygonCreator creator = new PolygonCreator(new Point2(x0, y0), numOfPoints, r);
        Point2 centreOfMass = creator.getCentreOfMass();
        PhysicalPolygon polygon = new PhysicalPolygon(this, v, w, centreOfMass.x, centreOfMass.y, creator.getPoints(), material);
        polygons.add(polygon);
        drawables.add(polygon);
    }

    public void addPolygon(Vector2 v, float w, float x0, float y0, int numOfPoints, float r) {
        PolygonCreator creator = new PolygonCreator(new Point2(x0, y0), numOfPoints, r);
        Point2 centreOfMass = creator.getCentreOfMass();
        PhysicalPolygon polygon = new PhysicalPolygon(this, v, w, centreOfMass.x, centreOfMass.y, creator.getPoints(), Material.CONSTANTIN);
        polygons.add(polygon);
        drawables.add(polygon);
    }

    public void focusCameraOnObject(Focusable objectToFocus){
        camera.setFocusedObject(objectToFocus);
    }

    public synchronized void deleteDynamicObjects(){
        for (PhysicalPolygon polygon : polygons) drawables.remove(polygon);
        for (PhysicalSphere sphere : spheres) drawables.remove(sphere);
        polygons.clear();
        spheres.clear();
    }

}
