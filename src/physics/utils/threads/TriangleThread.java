package physics.utils.threads;

import physics.physics.Space;
import physics.triangle.AST;

public class TriangleThread extends Thread {
    private Space space;

    public TriangleThread(Space space){
        this.space = space;
    }

    @Override
    public void run(){
            for (AST triangle : space.triangles) {
                synchronized (triangle) {
                    triangle.changeCord();
                }
            }
    }
}
