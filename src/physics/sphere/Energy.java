package physics.sphere;

public class Energy {
    private double wp, wk, g;
    private ASS thing;
    private Space space;

    Energy(ASS ast, double g, Space space) {
        this.g = g;
        this.thing = ast;
        this.space = space;
        this.change();
    }

    public void change() {
        wp = thing.m * g * (space.height - thing.y0);
        wk = (thing.m * (thing.v.getX() * thing.v.getX() + thing.v.getY() * thing.v.getY())) / 2.0;
    }

    public double count() {
        return wk + wp;
    }
}
