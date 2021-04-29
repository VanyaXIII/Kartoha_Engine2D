package climber_example;

import Kartoha_Engine2D.physics.Block;
import Kartoha_Engine2D.physics.Wall;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.utils.JsonAble;

import java.util.ArrayList;

public class Level implements JsonAble{

    private final ArrayList<Block> blocks;
    private final ArrayList<Wall> walls;
    private PhysicalSphere sphere;

    {
        sphere = null;
        blocks = new ArrayList<>();
        walls = new ArrayList<>();
    }

    public Level(PhysicalSphere sphere){
        this.sphere = sphere;
    }

    public Level() {

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
