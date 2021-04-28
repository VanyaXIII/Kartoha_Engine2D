package Kartoha_Engine2D.physics;

import Kartoha_Engine2D.limiters.Collisional;

public interface Collider<FirstThingType extends Collisional, SecondThingType extends Collisional>{

    void collide(FirstThingType firstThing, SecondThingType secondThing);

}
