package climber_example;

import Kartoha_Engine2D.physics.Block;
import Kartoha_Engine2D.physics.Space;
import Kartoha_Engine2D.physics.Wall;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.utils.JsonAble;
import climber_example.boosters.Booster;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


@Getter
@Setter
public class Level implements JsonAble{

    private ArrayList<Block> blocks;
    private ArrayList<Wall> walls;
    transient private PhysicalSphere sphere;
    private float g;
    private ArrayList<Booster> boosters;

    {
        sphere = null;
        boosters = new ArrayList<>();
        blocks = new ArrayList<>();
        walls = new ArrayList<>();
        g = 300;
    }

    public Level(Space space){
        this.sphere = space.getSpheres().get(0);
        this.walls = space.getWalls();
        this.blocks = space.getBlocks();
        this.g = space.getG();
    }

    public Level(Space space, ArrayList<Booster> boosters){
        this(space);
        this.boosters = boosters;
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

    public void setG(float g) {
        this.g = g;
    }

    public float getG() {
        return g;
    }
}
