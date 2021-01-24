package physics.utils;

import physics.geometry.Point2;
import physics.geometry.Vector2;

public class Tools {

    public static int transformFloat(float d) {
        return (int) Math.round(d);
    }

    public static float countAverage(float a, float b) {
        return (a + b) / 2.0f;
    }

    public static float sign(float a){
        return a>=0f ? 1f :-1f;
    }

}
