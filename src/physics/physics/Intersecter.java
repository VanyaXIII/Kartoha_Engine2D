package physics.physics;

import physics.geometry.Intersectional;

public interface Intersecter {
    boolean isIntersected(Intersectional thing1, Intersectional thing2);
}
