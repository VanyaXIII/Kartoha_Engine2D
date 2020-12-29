package physics.drawing;

import physics.geometry.Point2;
import physics.utils.Tools;

import java.awt.*;
import java.util.ArrayList;

public class ArbitraryFigure {

    private ArrayList<Point2> points;

    public ArbitraryFigure(ArrayList<Point2> points){
        this.points = points;
    }

    public Polygon getPolygon(){
        int[] xs = new int[points.size()];
        int[] ys = new int[points.size()];
        for (int i = 0; i < points.size(); i++){
            xs[i] = Tools.transformFloat(points.get(i).x);
            ys[i] = Tools.transformFloat(points.get(i).y);
        }
        return new Polygon(xs, ys, points.size()) ;
    }
}
