package climber_example.level_creator;

import Kartoha_Engine2D.drawing.Drawable;
import Kartoha_Engine2D.geometry.AABB;
import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.ui.Clickable;
import Kartoha_Engine2D.utils.Tools;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ReferencePoint implements Clickable, Drawable {

    private final Point2 point;
    private final Container container;

    public ReferencePoint(Point2 point, Container container) {
        this.point = point;
        this.container = container;
    }

    @Override
    public boolean handleClick(MouseEvent event) {
        if (new AABB(
                new Point2(point.x - 7f, point.y - 7f),
                new Point2(point.x + 7f, point.y + 7f)).doesContainPoint(new Point2(event.getX() + container.getSpace().getCamera().getXMovement(), event.getY() + container.getSpace().getCamera().getYMovement()))){
            container.addPoint(point);
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(138, 0, 255, 255));
        g.fillOval(Tools.transformFloat(point.x - container.getSpace().getCamera().getXMovement() - 7f), Tools.transformFloat(point.y - container.getSpace().getCamera().getYMovement()- 7f), 14, 14);
    }

    @Override
    public int getZ() {
        return 1;
    }
}
