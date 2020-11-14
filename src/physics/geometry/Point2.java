package physics.geometry;

public class Point2 {
    public float x, y;
    private boolean fixed;

    public Point2(float x, float y) {
        this.x = x;
        this.y = y;
        this.fixed = false;
    }

    public Point2(float x, float y, boolean fixed) {
        this.x = x;
        this.y = y;
        this.fixed = fixed;
    }

    public void rotate(Point2 rotatePoint, float angle) {
        if (!fixed) {
            float sx = x;
            x = (float) ((sx - rotatePoint.x) * Math.cos(angle) - (this.y - rotatePoint.y) * Math.sin(angle) + rotatePoint.x);
            y = (float) ((sx - rotatePoint.x) * Math.sin(angle) + (this.y - rotatePoint.y) * Math.cos(angle) + rotatePoint.y);
        }
    }

//    public Point2 rotated(Point2 rotatePoint, double angle) {
//        if (!fixed) {
//            double nx = (x - rotatePoint.x) * Math.cos(angle) - (this.y - rotatePoint.y) * Math.sin(angle) + rotatePoint.x;
//            double ny = (x - rotatePoint.x) * Math.sin(angle) + (this.y - rotatePoint.y) * Math.cos(angle) + rotatePoint.y;
//            return new Point2(nx, ny);
//        }
//        else return new Point2(x, y);
//    }

    public void fix() {
        fixed = true;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void unfix() {
        fixed  = false;
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
