package climber_example;

import Kartoha_Engine2D.physics.Block;
import Kartoha_Engine2D.physics.Space;
import Kartoha_Engine2D.physics.Wall;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.utils.JsonAble;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Level implements JsonAble{

    private ArrayList<Block> blocks;
    private ArrayList<Wall> walls;
    transient private PhysicalSphere sphere;

    {
        sphere = null;
        blocks = new ArrayList<>();
        walls = new ArrayList<>();
    }

    public Level(Space space){
        this.sphere = space.getSpheres().get(0);
        this.walls = space.getWalls();
        this.blocks = space.getBlocks();
    }

    public Level(PhysicalSphere sphere){
        this.sphere = sphere;
    }

    public Level() {

    }

    public void save(){
        FileDialog dialog = new FileDialog((Frame) null);
        dialog.setVisible(true);
        String directory = dialog.getDirectory();
        String filename = dialog.getFile();
        dialog.dispose();
        if (directory != null && filename != null) {
            String path = directory + filename;
            try {
                File file = new File(path);
                PrintWriter writer = new PrintWriter(file);
                writer.println(toJson());
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    public void setSphere(PhysicalSphere sphere) {
        this.sphere = sphere;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public PhysicalSphere getSphere() {
        return sphere;
    }
}
