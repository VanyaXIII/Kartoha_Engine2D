package com.github.gurianov.engine.geometry;

import com.github.gurianov.engine.limiters.Intersectional;

public interface Intersecter {

    boolean areIntersected(Intersectional thing1, Intersectional thing2);

}
