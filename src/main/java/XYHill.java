public class XYHill {

    private Graph xRow;
    private Graph yRow;
    private Integer xMax;
    private Integer yMax;
    private boolean type = false; //false = A, true = B

    public XYHill(Graph xRow, Graph yRow) {
        this.xRow = xRow;
        this.yRow = yRow;
        this.xMax = xRow.findMax();
        this.yMax = yRow.findMax();
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

    public String toString() {
        return xRow.toString() + "\nMax:" + xMax + " \n" + yRow.toString() + "\nMax:" + yMax;
    }
}
