package KartohaEngine2D.geometry;

import KartohaEngine2D.limiters.Intersectional;

public interface Intersecter {

    boolean areIntersected(Intersectional thing1, Intersectional thing2);

}
