package physics.sphere;

import java.awt.*;

public class Material {
    public final double p;
    public final Color color;

    public Material(double p, Color color){
        this.p = p;
        this.color = color;
    }

    public static Material Steel = new Material(7900, new Color(175,175,175));
    public static Material Wood = new Material(500, new Color(124, 67 ,11));
    public static Material Stone = new Material(2500, new Color(90, 90, 90));
    public static Material Gold = new Material(19300, new Color(238, 198, 0));
    public static Material LapisLazuli = new Material(2500, new Color(0,34,255));
    public static Material Osmium = new Material(22500, new Color(148, 157, 191));

}
