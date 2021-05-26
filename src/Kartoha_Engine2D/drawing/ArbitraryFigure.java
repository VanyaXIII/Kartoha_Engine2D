package Kartoha_Engine2D.drawing;

import Kartoha_Engine2D.drawing.camera.Camera;
import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.utils.Tools;

import java.awt.*;
import java.util.ArrayList;

public class ArbitraryFigure {

    private final ArrayList<Point2> points;

    public ArbitraryFigure(ArrayList<Point2> points){
        this.points = points;
    }

    public final Polygon getPolygon(){
        int[] xs = new int[points.size()];
        int[] ys = new int[points.size()];
        for (int i = 0; i < points.size(); i++){
            xs[i] = Tools.transformFloat(points.get(i).x);
            ys[i] = Tools.transformFloat(points.get(i).y);
        }
        return new Polygon(xs, ys, points.size());
    }

    public final Polygon getPolygon(Camera camera){
        int[] xs = new int[points.size()];
        int[] ys = new int[points.size()];
        for (int i = 0; i < points.size(); i++){
            xs[i] = Tools.transformFloat(points.get(i).x - camera.getXMovement());
            ys[i] = Tools.transformFloat(points.get(i).y - camera.getYMovement());
        }
        return new Polygon(xs, ys, points.size());
    }
}
