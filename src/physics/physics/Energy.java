package physics.physics;

import physics.sphere.ASS;

public class Energy {
    public double wp, wk, g;
    private ASS thing;
    private Space space;

    public Energy(ASS ast, double g, Space space) {
        this.g = g;
        this.thing = ast;
        this.space = space;
        this.update();
    }

    public void update() {
        wp = thing.m * g * (space.height - thing.y0 - thing.r);
        wk = thing.m * (thing.v.getX() * thing.v.getX() + thing.v.getY() * thing.v.getY()) / 2.0;
    }

    public double count() {
        return wk + wp;
    }

    @Override
    public String toString(){
        return String.valueOf(count());
    }
}
