package Kartoha_Engine2D.drawing.camera;

import Kartoha_Engine2D.geometry.Point2;


public class Camera {

    private final Point2 min;
    private final Resolution resolution;

    public Camera(Point2 min, Resolution resolution) {
        this.min = min;
        this.resolution = resolution;
    }

    public void moveX(float x){
        this.min.x += x;
    }

    public void moveY(float y){
        this.min.y +=y;
    }

    public float getXMovement(){
        return this.min.x;
    }
    public float getYMovement(){
        return this.min.y;
    }
}
