import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Graph {

    private Integer xRow;
    private List<Integer> values = new ArrayList<Integer>();
    private boolean type; //false = A, true = B

    public Graph(Integer xRow) {
        this.xRow = xRow;
    }

    public Integer getxRow() {
        return xRow;
    }

    public void setxRow(Integer xRow) {
        this.xRow = xRow;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void addValue(Integer value) {
        values.add(value);
    }

    public Integer findMin() {
        int min = Integer.MAX_VALUE;
        for (Integer i : values) min = min < i ? min : i;
        return min;
    }

    public Integer findMax() {
        int max = Integer.MIN_VALUE;
        for (Integer i : values) max = max > i ? max : i;
        return max;
    }

    public double riseProcent() {
        return round((100.0 / (Double.valueOf(values.get(0))) * findMax()) - 100.00, 2);
    }

    public double fallProcent() {
        return round((100.0 / (Double.valueOf(values.get(values.size()-1))) * findMax()) - 100.00 , 2);
    }

    @Override
    public String toString() {
        return "x = " + xRow + " - Graph{" +
                "values=" + values +
                "}\n";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
