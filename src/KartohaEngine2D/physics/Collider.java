package KartohaEngine2D.physics;

import KartohaEngine2D.limiters.Collisional;

public interface Collider<FirstThingType extends Collisional, SecondThingType extends Collisional>{

    void collide(FirstThingType firstThing, SecondThingType secondThing);

}
