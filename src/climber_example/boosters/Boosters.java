package climber_example.boosters;


import Kartoha_Engine2D.sphere.PhysicalSphere;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Boosters {

    private final PhysicalSphere applicableSphere;
    private final int duration;

    public Boosters(PhysicalSphere applicableSphere, int duration) {
        this.applicableSphere = applicableSphere;
        this.duration = duration;
    }

    public Booster getVelocityBooster(){
        return new Booster(duration, new Applicable() {
            @Override
            public void apply() {
               applicableSphere.setV(applicableSphere.getV().getMultipliedVector(3));
            }

            @Override
            public void disable() {
                applicableSphere.setV(applicableSphere.getV().getMultipliedVector(3));
            }
        });
    }
}
