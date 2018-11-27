public class XYPoint {

    private Integer x;
    private Integer y;

    public XYPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public String toString() {
        return "XYPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
