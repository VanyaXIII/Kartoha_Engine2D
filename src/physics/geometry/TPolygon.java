package physics.geometry;

import physics.utils.Tools;

import java.awt.*;

public class TPolygon extends Polygon {
    public TPolygon(Point2 point1, Point2 point2, Point2 point3){
        super(new int[]{Tools.transformDouble(point1.x), Tools.transformDouble(point2.x), Tools.transformDouble(point3.x)},
                new int[]{Tools.transformDouble(point1.y), Tools.transformDouble(point2.y), Tools.transformDouble(point3.y)}, 3);
    }
}
