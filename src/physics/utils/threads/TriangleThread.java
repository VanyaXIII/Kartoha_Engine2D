package physics.utils.threads;

import physics.sphere.Space;
import physics.triangle.AST;
import physics.triangle.Triangle;

public class TriangleThread extends Thread {
    private Space space;

    public TriangleThread(Space space){
        this.space = space;
    }

    @Override
    public void run(){
        for (AST triangle : space.triangles) {
            space.tlines.clear();
            triangle.changeCoord();
            space.fillTLines();
        }
    }
}
