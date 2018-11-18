public class XYHill {

    private Graph xRow;
    private Graph yRow;
    private Integer x;
    private Integer y;
    private boolean type = false; //false = A, true = B

    public XYHill(Graph xRow, Graph yRow, Integer x, Integer y) {
        this.xRow = xRow;
        this.yRow = yRow;
        this.x = x;
        this.y = y;
    }

    public Graph getxRow() {
        return xRow;
    }

    public void setxRow(Graph xRow) {
        this.xRow = xRow;
    }

    public Graph getYrow() {
        return yRow;
    }

    public void setYrow(Graph yRow) {
        this.yRow = yRow;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Graph getyRow() {
        return yRow;
    }

    public void setyRow(Graph yRow) {
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
        return xRow.toString() + "\nMax:" + x + " \n" + yRow.toString() + "\nMax:" + y;
    }
}
