package physics.sphere;

import java.awt.*;

public class Material {
    public final double p;
    public final Color color;

    Material(double p, Color color){
        this.p = p;
        this.color = color;
    }

    public static Material Steel = new Material(7900, Color.GRAY);
    public static Material Wood = new Material(500, Color.YELLOW);
    public static Material Stone = new Material(2500, Color.DARK_GRAY);

}
