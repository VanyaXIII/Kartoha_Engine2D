package physics.utils.threads;

import physics.sphere.ASS;
import physics.sphere.Space;

public class SphereThread extends Thread{
    private Space space;

    public SphereThread(Space space){
        this.space = space;
    }

    @Override
    public void run(){
        for (ASS sphere : space.spheres){
            sphere.changeCoord();
        }
    }
}
