package climber_example.boosters;


import Kartoha_Engine2D.geometry.Vector2;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class Boosters {

    private final PhysicalSphere applicableSphere;
    private final int duration;
    private Booster jumper;
    private Booster accelerator;
    private Booster stopper;
    private Map<String, Booster> boosterMap;

    public Boosters(PhysicalSphere applicableSphere, int duration) {
        this.applicableSphere = applicableSphere;
        this.duration = duration;
        jumper = getJumper();
        accelerator = getAccelerator();
        stopper = getStopper();
        boosterMap = new HashMap<>();
        boosterMap.put("Jumper", jumper);
        boosterMap.put("Accelerator", accelerator);
        boosterMap.put("Stopper", stopper);
    }



    public Booster getJumper(){
        return new Booster(duration, new Applicable() {
            @Override
            public void apply() {
                applicableSphere.setV(new Vector2(applicableSphere.getV().getX(),-500));
                applicableSphere.setW(0);
            }

            @Override
            public void disable() {
            }
        });
    }

    public Booster getAccelerator(){
        return new Booster(duration, new Applicable() {
            @Override
            public void apply() {
                applicableSphere.setV(new Vector2(3*applicableSphere.getV().getX(),0));
                applicableSphere.setW(0);
            }

            @Override
            public void disable() {
            }
        });
    }

    public Booster getStopper(){
        return new Booster(duration, new Applicable() {
            @Override
            public void apply() {
                applicableSphere.setV(new Vector2(0,0));
                applicableSphere.setW(0);
            }
            @Override
            public void disable() {
            }
        });
    }


}
