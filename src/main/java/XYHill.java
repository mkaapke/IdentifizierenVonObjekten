public class XYHill {

    private XYGraph xValues;
    private XYGraph yValues;
    private Integer x;
    private Integer y;
    private boolean type = false; //false = A, true = B

    public XYHill(XYGraph xValues, XYGraph yValues, Integer x, Integer y) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.x = x;
        this.y = y;
    }

    public XYGraph getxValues() {
        return xValues;
    }

    public void setxValues(XYGraph xValues) {
        this.xValues = xValues;
    }

    public XYGraph getYrow() {
        return yValues;
    }

    public void setYrow(XYGraph yRow) {
        this.yValues = yRow;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public XYGraph getyValues() {
        return yValues;
    }

    public void setyValues(XYGraph yValues) {
        this.yValues = yValues;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String toString() {
        return xValues.toString() + "\nX:" + x + " \n" + yValues.toString() + "\nY:" + y;
    }
}
