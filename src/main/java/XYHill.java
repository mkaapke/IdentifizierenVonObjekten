/**
 * Der XYHill repräsentiert einen Hügel da, der aus einem x-Graphen und einen y-Graphen besteht.
 * So ist es möglich, den Hügel von zwei Seiten aus zu betrachten.
 * Außerdem wird der Höhepunkt als x und y Koordinate gespeichert.
 */
public class XYHill {

    private XYGraph xValues; //XYGraph für die x-Ansicht
    private XYGraph yValues; //XYGraph für die y-Ansicht
    private Integer x; //x-Koordinate
    private Integer y; //y-Koordinate

    /**
     * Der XYHill erwartet einen XYGraph, der die x-Ansicht darstellt und einen XYGraph für die y-Ansicht.
     * Die x und y Koordinate ist der Höhepunkt des XYHill´s.
     * @param xValues - Die x-Ansicht des XYHill´s
     * @param yValues - Die y-Ansicht des XYHill´s
     * @param x - x-Koordiante des Höhepunktes
     * @param y - y-Koordiante des Höhepunktes
     */
    public XYHill(XYGraph xValues, XYGraph yValues, Integer x, Integer y) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.x = x;
        this.y = y;
    }

    public XYGraph getxValues() {
        return xValues;
    }

    public XYGraph getyValues() {
        return yValues;
    }

    /**
     * Gibt den XYHill als String aus.
     * @return - XYHill als String.
     */
    public String toString() {
        return "X:" + x + xValues + " \n" + "Y:" + y + yValues + "\n";
    }
}
