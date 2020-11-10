package physics.geometry;

public class Vector2 {
    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector1, Vector2 vector2) {
        this.x = vector1.x + vector2.x;
        this.y = vector1.y + vector2.y;
    }

    public Vector2(Point2 point1, Point2 point2){
        this.x = point2.x - point1.x;
        this.y = point2.y - point1.y;
    }

    public Vector2(Line line){
        this.x = line.x2 - line.x1;
        this.y = line.y2 - line.y1;
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addY(double a) {
        y += a;
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

    public Vector2 createOpVect(){
        return new Vector2(-x, -y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public void makeUnit() {
        double d = this.length();
        x /= d;
        y /= d;
    }

    public Vector2 createByDouble(double l) {
        double d = this.length();
        return new Vector2((x/d)*l, (y/d)*l);
    }

    public double dot(Vector2 vector) {
        return this.getY() * vector.getY() + this.getX() * vector.getX();
    }

    public void mul(double number) {
        this.x *= number;
        this.y *= number;
    }

    public Point2 movePoint(Point2 point, double movement){
        Vector2 mv = createByDouble(movement);
        return new Point2(point.x + mv.getX(), point.y + mv.getY());

    }

    public void rotate(double angle) {
        double x1, y1;
        x1 = x;
        y1 = y;
        x = x1 * Math.cos(angle) - y1 * Math.sin(angle);
        y = y1 * Math.cos(angle) + x1 * Math.sin(angle);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2 createNormal() {
        return new Vector2(-y, x);
    }

    public double countProjectionOn(Vector2 vector) {
        double projection = this.dot(vector) / vector.length();
        return projection;
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
