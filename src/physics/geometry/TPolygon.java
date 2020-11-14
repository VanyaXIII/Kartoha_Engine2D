package physics.geometry;

import physics.utils.Tools;

import java.awt.*;

public class TPolygon extends Polygon {
    public TPolygon(Point2 point1, Point2 point2, Point2 point3){
        super(new int[]{Tools.transformFloat(point1.x), Tools.transformFloat(point2.x), Tools.transformFloat(point3.x)},
                new int[]{Tools.transformFloat(point1.y), Tools.transformFloat(point2.y), Tools.transformFloat(point3.y)}, 3);
    }
}
