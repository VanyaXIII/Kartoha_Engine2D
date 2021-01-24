package physics.physics;

import physics.limiters.Collisional;

public interface Collider<FirstThingType extends Collisional, SecondThingType extends Collisional>{
    void collide(FirstThingType firstThing, SecondThingType secondThing);
}
