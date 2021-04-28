package Kartoha_Engine2D.geometry;

import Kartoha_Engine2D.limiters.Intersectional;

public interface Intersecter {

    boolean areIntersected(Intersectional thing1, Intersectional thing2);

}
