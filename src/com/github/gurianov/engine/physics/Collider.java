package com.github.gurianov.engine.physics;

import com.github.gurianov.engine.limiters.Collisional;

public interface Collider<FirstThingType extends Collisional, SecondThingType extends Collisional>{

    void collide(FirstThingType firstThing, SecondThingType secondThing);

}
