public class XYHill {

    private XYGraph xValues;
    private XYGraph yValues;
    private Integer x;
    private Integer y;

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

    public String toString() {
        return xValues.toString() + "\nX:" + x + " \n" + yValues.toString() + "\nY:" + y;
    }
}
