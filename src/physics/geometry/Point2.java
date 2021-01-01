package physics.geometry;

public class Point2 {
    public float x, y;

    public Point2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(Point2 rotatePoint, float angle) {
        float sx = x;
        x = (float) ((sx - rotatePoint.x) * Math.cos(angle) - (this.y - rotatePoint.y) * Math.sin(angle) + rotatePoint.x);
        y = (float) ((sx - rotatePoint.x) * Math.sin(angle) + (this.y - rotatePoint.y) * Math.cos(angle) + rotatePoint.y);

    }

//    public Point2 rotated(Point2 rotatePoint, double angle) {
//        if (!fixed) {
//            double nx = (x - rotatePoint.x) * Math.cos(angle) - (this.y - rotatePoint.y) * Math.sin(angle) + rotatePoint.x;
//            double ny = (x - rotatePoint.x) * Math.sin(angle) + (this.y - rotatePoint.y) * Math.cos(angle) + rotatePoint.y;
//            return new Point2(nx, ny);
//        }
//        else return new Point2(x, y);
//    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
