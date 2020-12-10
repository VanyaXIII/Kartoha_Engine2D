package physics.geometry;

public class SphereIntersection {
    public final boolean isIntersected;
    public Vector2 centralLine;
    private float value;

    public SphereIntersection(boolean isIntersected, Vector2 cl, float value) {
        this.isIntersected = isIntersected;
        this.centralLine = cl;
        this.value = value;
    }

    public SphereIntersection(boolean isIntersected) {
        this.isIntersected = isIntersected;
    }

    public float getValue() {
        return value;
    }
}
