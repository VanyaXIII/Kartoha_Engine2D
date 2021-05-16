package Kartoha_Engine2D.physics;

import java.awt.*;

public enum Material {

    STEEL(7900, new Color(175, 175, 175),0.5f, 0.6f, "Сталь"),
    STONE(2500, new Color(90, 90, 90),1f, 0.5f, "Камень"),
    GOLD(19300, new Color(238, 198, 0),0.7f, 0.0f, "Золото"),
    CONSTANTIN(1000, Color.RED, 0.7f, 0.7f, "Константин"),
    OSMIUM(22500, new Color(148, 157, 191),1f, 0f, "Осмий"),
    WOOD(500, new Color(124, 67, 11),0.5f, 0.2f, "Дерево"),
    LAZULI(2500, new Color(0, 34, 255), 0.9f, 0.1f, "Лазурит"),
    SAND(2600, new Color(239, 239, 105), 0.1f, 0.4f, "Песок"  ),
    RED_STONE(2500, new Color(236, 81, 81),1f, 0.5f, "Красный Камень"),
    ICE(900, Color.white, 0.5f,0.1f, "Лед"),
    DIRT(1450, new Color(0x532B03), 0.1f, 0.9f, "Земля"),
    GUM(1100, new Color(0xFF36FC), 0.9f, 0.7f, "Резина" );


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
