package climber_example.level_creator;

import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.physics.Block;
import Kartoha_Engine2D.physics.Material;
import Kartoha_Engine2D.physics.Space;
import Kartoha_Engine2D.physics.Wall;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.utils.Pair;
import climber_example.Level;
import climber_example.boosters.Booster;
import climber_example.boosters.Boosters;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Container {

    @Getter
    private final Space space;
    @Getter @Setter
    private String boosterName;
    private AddingMode addingMode;
    @Getter @Setter
    private float g;
    @Getter
    private ArrayList<Wall> walls;
    @Getter
    private ArrayList<Block> blocks;
    @Getter
    private ArrayList<Pair<String, PhysicalSphere>> boosterPairs;
    private final ArrayList<ReferencePoint> points;
    @Getter
    private Material currentMaterial;
    private Point2 point1, point2;

    {
        points = new ArrayList<>();
        boosterName = "Jumper";
        addingMode = AddingMode.BLOCKS;
        point1 = null;
        point2 = null;
        walls = new ArrayList<>();
        blocks = new ArrayList<>();
        currentMaterial = Material.CONSTANTIN;
        boosterPairs = new ArrayList<>();
        g = 300f;
    }


    public Container(Space space){
        this.space = space;
    }

    private void addWall(){
        space.addWall(point1.x, point1.y, point2.x, point2.y, currentMaterial);
        walls.add(new Wall(point1.x, point1.y, point2.x, point2.y, currentMaterial, space));
        point1 = null;
        point2 = null;
    }

    private void addBlock(){
        space.addBlock(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y), Math.abs(point2.x - point1.x), Math.abs(point2.y - point1.y), currentMaterial);
        blocks.add(new Block(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y), Math.abs(point2.x - point1.x), Math.abs(point2.y - point1.y), currentMaterial, space));
        point1 = null;
        point2 = null;
    }

    private void addBooster(Point2 point){
        PhysicalSphere sphere = new PhysicalSphere(space, point.x, point.y, 50);
        space.getDrawables().add(sphere);
        boosterPairs.add(new Pair<>(boosterName, sphere));
    }

    public void addPoint(Point2 point){
        if (addingMode == AddingMode.BOOSTERS){
            addBooster(point);
        }
        if (point1 != null && point2 == null){
            point2 = point;
            if (addingMode == AddingMode.WALLS)
                addWall();
            else if (addingMode == AddingMode.BLOCKS)
                addBlock();
        }
        else point1 = point;
    }

    public void setCurrentMaterial(Material currentMaterial) {
        this.currentMaterial = currentMaterial;
    }

    public Level getLevel(){
        Level level = new Level();
        for (Wall wall : walls)
            level.getWalls().add(wall);
        for (Block block : blocks)
            level.getBlocks().add(block);
        level.setG(g);
        level.setBoosterPairs(boosterPairs);
        return level;
    }

    public void setLevel(Level level){
        this.walls = level.getWalls();
        this.blocks = level.getBlocks();
        points.clear();
        space.getWalls().clear();
        space.getBlocks().clear();
        space.setG(level.getG());
        walls.forEach(wall -> wall.setSpace(space));
        blocks.forEach(block -> block.setSpace(space));
        for (Wall wall : walls){
            space.getDrawables().add(wall);
            space.getWalls().add(wall);
        }
        for (Block block : blocks){
            space.addBlock(block.getX(), block.getY(), block.getW(), block.getH(), block.getMaterial());
        }
        boosterPairs = level.getBoosterPairs();
        boosterPairs.forEach(stringPhysicalSpherePair -> {
            stringPhysicalSpherePair.getValue().setSpace(space);
            space.getDrawables().add(stringPhysicalSpherePair.getValue());
            Boosters boosters = new Boosters(space.getSpheres().get(0), 0);
            Booster booster = boosters.getBoosterMap().get(stringPhysicalSpherePair.getKey());
            booster.setDrawingParams(stringPhysicalSpherePair.getValue());
            space.getExecutables().add(booster.getExecutable(space.getSpheres().get(0)));
        });
        point1 = null;
        point2 = null;
    }

    public void deleteAllObjects(){
        walls.clear();
        blocks.clear();
        space.getWalls().clear();
        space.getBlocks().clear();
        space.getDrawables().clear();
        for (ReferencePoint point : points){
            space.getDrawables().remove(point);
        }
        points.clear();
    }

    public void changeAddingMode(){
        if (addingMode == AddingMode.BLOCKS) {
            addingMode = AddingMode.WALLS;
            return;
        }
        if (addingMode == AddingMode.WALLS){
            addingMode = AddingMode.BOOSTERS;
            return;
        }
        if (addingMode == AddingMode.BOOSTERS) {
            addingMode = AddingMode.BLOCKS;
        }
    }

    public ArrayList<ReferencePoint> getPoints() {
        return points;
    }

    public void addReferencePoint(Point2 point){
        ReferencePoint referencePoint = new ReferencePoint(point, this);
        points.add(referencePoint);
        space.getDrawables().add(referencePoint);
    }
}
