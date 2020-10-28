package physics.geometry;

public class Vector2D {
    private double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector1, Vector2D vector2) {
        this.x = vector1.x + vector2.x;
        this.y = vector1.y + vector2.y;
    }

    public Vector2D(Point2D point1, Point2D point2){
        this.x = point2.x - point1.x;
        this.y = point2.y - point1.y;
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addY(double a) {
        this.y += a;
    }

    public void addX(double a) {
        this.x += a;
    }

    public void makeOpX() {
        this.x = -this.x;
    }

    public void makeOpY() {
        this.y = -this.y;
    }

    public void makeOpVect(){
        makeOpX();
        makeOpY();
    }

    public Vector2D createOpVect(){
        return new Vector2D(-x, -y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public void makeUnit() {
        double d = this.length();
        x /= d;
        y /= d;
    }

    public Vector2D createByDouble(double l) {
        double d = this.length();
        return new Vector2D((x/d)*l, (y/d)*l);
    }

    public double dot(Vector2D vector) {
        return this.getY() * vector.getY() + this.getX() * vector.getX();
    }

    public void mul(double number) {
        this.x *= number;
        this.y *= number;
    }

    public Point2D movePoint(Point2D point, double movement){
        Vector2D mv = createByDouble(movement);
        return new Point2D(point.x + mv.getX(), point.y + mv.getY());

    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2D createNormal() {
        return new Vector2D(-y, x);
    }

    public double countProjectionOn(Vector2D vector) {
        double projection = this.dot(vector) / vector.length();
        return projection;
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
