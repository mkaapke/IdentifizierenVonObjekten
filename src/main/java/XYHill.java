public class XYHill {

    private XYGraph xRow;
    private XYGraph yRow;
    private Integer x;
    private Integer y;
    private boolean type = false; //false = A, true = B

    public XYHill(XYGraph xRow, XYGraph yRow, Integer x, Integer y) {
        this.xRow = xRow;
        this.yRow = yRow;
        this.x = x;
        this.y = y;
    }

    public XYGraph getxRow() {
        return xRow;
    }

    public void setxRow(XYGraph xRow) {
        this.xRow = xRow;
    }

    public XYGraph getYrow() {
        return yRow;
    }

    public void setYrow(XYGraph yRow) {
        this.yRow = yRow;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public XYGraph getyRow() {
        return yRow;
    }

    public void setyRow(XYGraph yRow) {
        this.yRow = yRow;
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
        return xRow.toString() + "\nX:" + x + " \n" + yRow.toString() + "\nY:" + y;
    }
}
