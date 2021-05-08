package Kartoha_Engine2D.physics;

import java.awt.*;

public enum Material {

    STEEL(7900, new Color(175, 175, 175),0.5f, 0.6f, "Сталь"),
    STONE(2500, new Color(90, 90, 90),1f, 0.5f, "Камень"),
    GOLD(19300, new Color(238, 198, 0),0.7f, 0.0f, "Золото"),
    CONSTANTIN(1000, Color.RED, 0.7f, 0.7f, "Константин"),
    OSMIUM(22500, new Color(148, 157, 191),1f, 0f, "Осмий"),
    WOOD(500, new Color(124, 67, 11),0.5f, 0.5f, "Дерево"),
    LAZULI(2500, new Color(0, 34, 255), 0.9f, 0.1f, "Лазурит");

    public final float p;
    public final Color outlineColor, fillColor;
    public final float coefOfReduction;
    public final float coefOfFriction;
    private final String name;

    Material(float p, Color outlineColor, float coefOfReduction, float coefOfFriction, String name) {
        this.name = name;
        this.p = p;
        this.outlineColor = outlineColor;
        this.coefOfReduction = coefOfReduction;
        this.coefOfFriction = coefOfFriction;
        this.fillColor = new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), 170);
    }

    public String getName() {
        return name;
    }
}
