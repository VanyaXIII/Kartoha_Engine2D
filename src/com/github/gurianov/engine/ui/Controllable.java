package com.github.gurianov.engine.ui;

import com.github.gurianov.engine.geometry.Point2;
import com.github.gurianov.engine.geometry.Vector2;

public interface Controllable {
    void setV(Vector2 v);
    void setW(float w);
    void rotate(float a);
    void move(Vector2 movement);
    void setCords(Point2 newCords);
}
