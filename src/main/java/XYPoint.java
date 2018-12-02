/**
 * Stellt einen Punkt in einer XY-Matrix da.
 */
public class XYPoint {

    public Integer x; //x-Koordinate

    public Integer y; //y-Koordinate

    /**
     * Der Konstruktor erwartet eine x und y Koordinate als Integer.
     * @param x - x-Koordinate als Integer.
     * @param y - y-Koordinate als Integer.
     */
    public XYPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gibt den XYPoint als String aus.
     * @return - XYPoint als String.
     */
    public String toString() {
        return "XYPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
