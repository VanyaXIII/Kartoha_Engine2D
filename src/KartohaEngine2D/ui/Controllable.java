package KartohaEngine2D.ui;

import KartohaEngine2D.geometry.Point2;
import KartohaEngine2D.geometry.Vector2;

public interface Controllable {
    void setV(Vector2 v);
    void setW(float w);
    void rotate(float a);
    void move(Vector2 movement);
    void setCords(Point2 newCords);
}
