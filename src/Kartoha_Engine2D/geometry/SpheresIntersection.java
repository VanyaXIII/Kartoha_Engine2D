package Kartoha_Engine2D.geometry;

import lombok.Getter;

public class SpheresIntersection {

    public final boolean isIntersected;
    public Vector2 centralLine;
    @Getter
    private float value;

    public SpheresIntersection(boolean isIntersected, Vector2 cl, float value) {
        this.isIntersected = isIntersected;
        this.centralLine = cl;
        this.value = value;
    }

    public SpheresIntersection(boolean isIntersected) {
        this.isIntersected = isIntersected;
    }

}
