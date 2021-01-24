package physics.physics;

import physics.limiters.Intersectional;

public interface Intersecter {
    boolean isIntersected(Intersectional thing1, Intersectional thing2);
}
