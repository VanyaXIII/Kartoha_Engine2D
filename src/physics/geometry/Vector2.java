package physics.geometry;

public class Vector2 {
    private float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector1, Vector2 vector2) {
        this.x = vector1.x + vector2.x;
        this.y = vector1.y + vector2.y;
    }

    public Vector2(Point2 point1, Point2 point2) {
        this.x = point2.x - point1.x;
        this.y = point2.y - point1.y;
    }

    public Vector2(Line line) {
        this.x = line.x2 - line.x1;
        this.y = line.y2 - line.y1;
    }


    public void mul(float m) {
        x *= m;
        y *= m;
    }

    public Vector2 getMultipliedVector(float mul) {
        return new Vector2(x * mul, y * mul);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void addY(float a) {
        y += a;
    }

    public void addX(float a) {
        this.x += a;
    }

    public void makeOpX() {
        this.x = -this.x;
    }

    public void makeOpY() {
        this.y = -this.y;
    }

    public void makeOp() {
        makeOpX();
        makeOpY();
    }

    public float getSquare() {
        return x * x + y * y;
    }

    public Vector2 createOpVect() {
        return new Vector2(-x, -y);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void makeUnit() {
        double d = this.length();
        x /= d;
        y /= d;
    }

    public Vector2 createByFloat(float l) {
        float d = this.length();
        return new Vector2((x / d) * l, (y / d) * l);
    }

    public float dot(Vector2 vector) {
        return this.getY() * vector.getY() + this.getX() * vector.getX();
    }

    public void mul(double number) {
        this.x *= number;
        this.y *= number;
    }

    public Point2 movePoint(Point2 point, float movement) {
        Vector2 mv = createByFloat(movement);
        return new Point2(point.x + mv.getX(), point.y + mv.getY());

    }

    public void rotate(float angle) {
        double x1, y1;
        x1 = x;
        y1 = y;
        x = (float) (x1 * Math.cos(angle) - y1 * Math.sin(angle));
        y = (float) (y1 * Math.cos(angle) + x1 * Math.sin(angle));
    }

    public Vector2 getRotatedVector(float angle) {
        float x1, y1;
        x1 = (float) (x * Math.cos(angle) - y * Math.sin(angle));
        y1 = (float) (y * Math.cos(angle) + x * Math.sin(angle));
        return new Vector2(x1, y1);
    }

    public void setLength(float len) {
        float d = length();
        x = (x / d) * len;
        y = (y / d) * len;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 createNormal() {
        return new Vector2(-y, x);
    }

    public float countProjectionOn(Vector2 vector) {
        return this.dot(vector) / vector.length();
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
