package climber_example.boosters;


import Kartoha_Engine2D.drawing.Drawable;
import Kartoha_Engine2D.geometry.IntersectionalPair;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
@Setter
public class Booster implements Drawable {

    private int duration;
    private boolean applied;
    private final Applicable applicable;
    private PhysicalSphere drawingParams;
    private BufferedImage sprite;

    {
        applied = false;
        drawingParams = null;
        sprite = null;
    }

    public Booster(int duration, Applicable applicable) {
        this.duration = duration;
        this.applicable = applicable;
    }

    public boolean checkCollision(PhysicalSphere sphere){
        return new IntersectionalPair<>(drawingParams, sphere).areIntersected();
    }

    public void apply(){
        applicable.apply();
        applied = true;
    }

    public void disable(){
        applicable.disable();
    }

    public void check(){
       if (duration == 0 && applied)
           applicable.disable();
    }

    public void reduceDuration(){
        if (applied) duration--;
    }

    @Override
    public void draw(Graphics g) {
        if (drawingParams != null){
            drawingParams.draw(g);
        }
    }

    @Override
    public int getZ() {
        return 0;
    }
}
