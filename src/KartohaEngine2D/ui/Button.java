package KartohaEngine2D.ui;

import KartohaEngine2D.geometry.Point2;

import java.awt.*;

public class Button {

    private final Clickable clickHandler;
    private final Color color;
    private final Point2 lowerLeftCorner;
    private final float width,height;
    private final String text;

    public Button(Clickable clickHandler, Color color, Point2 lowerLeftCorner, float width, float height, String text) {
        this.clickHandler = clickHandler;
        this.color = color;
        this.lowerLeftCorner = lowerLeftCorner;
        this.width = width;
        this.height = height;
        this.text = text;
    }
}
