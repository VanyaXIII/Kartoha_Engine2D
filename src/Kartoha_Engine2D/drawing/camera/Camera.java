package Kartoha_Engine2D.drawing.camera;

import Kartoha_Engine2D.geometry.Point2;


public class Camera {

    private Point2 min;
    private final Resolution resolution;
    private Focusable focusedObject;

    {
        focusedObject = null;
    }

    public Camera(Point2 min, Resolution resolution) {
        this.min = min;
        this.resolution = resolution;
    }

    private void setCentre(Point2 centre){
        if (resolution != null) min = new Point2(centre.x - resolution.width / 2f, centre.y - resolution.height / 2f);
    }

    public void centre(){
        if (focusedObject != null) setCentre(focusedObject.getPositionOfCentre());
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

    public Focusable getFocusedObject() {
        return focusedObject;
    }

    public Point2 getMin() {
        return min;
    }

    public void setFocusedObject(Focusable focusedObject) {
        this.focusedObject = focusedObject;
    }

    public Resolution getResolution() {
        return resolution;
    }
}
