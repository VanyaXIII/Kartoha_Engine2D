package Kartoha_Engine2D.drawing.camera;

import Kartoha_Engine2D.geometry.Point2;
import lombok.Getter;
import lombok.Setter;


public class Camera {

    @Getter
    private Point2 min;
    @Getter
    private final Resolution resolution;
    @Getter @Setter
    private Focusable focusedObject;

    {
        focusedObject = null;
    }

    public Camera(Point2 min, Resolution resolution) {
        this.min = min;
        this.resolution = resolution;
    }

    private void setCentre(Point2 centre) {
        if (resolution != null) min = new Point2(centre.x - resolution.width / 2f, centre.y - resolution.height / 2f);
    }

    public void centre() {
        if (focusedObject != null) setCentre(focusedObject.getPositionOfCentre());
    }

    public void moveX(float x) {
        this.min.x += x;
    }

    public void moveY(float y) {
        this.min.y += y;
    }

    public float getXMovement() {
        return this.min.x;
    }

    public float getYMovement() {
        return this.min.y;
    }
}
