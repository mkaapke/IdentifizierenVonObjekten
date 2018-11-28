/**
 * Stellt einen Punkt in einer XY-Matrix da.
 */
public class XYPoint {

    public Integer x;

    public Integer y;

    public XYPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "XYPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
