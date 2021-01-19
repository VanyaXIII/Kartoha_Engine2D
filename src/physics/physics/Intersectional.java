package physics.physics;

import physics.geometry.Collisional;

public interface Intersectional {
    boolean isIntersected(Collisional thing1, Collisional thing2);
}
