import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class XYGraph {

    private Integer row;
    private List<Integer> valuesZ = new ArrayList<Integer>();
    private List<Integer> valuesY = new ArrayList<Integer>();
    private boolean type; //false = A, true = B

    public XYGraph(Integer row) {
        this.row = row;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public List<Integer> getValuesZ() {
        return valuesZ;
    }

    public void setValuesZ(List<Integer> valuesZ) {
        this.valuesZ = valuesZ;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void addValueZ(Integer value) {
        valuesZ.add(value);
    }

    public void addValueY(Integer value) {
        valuesY.add(value);
    }

    public Integer findMin() {
        int min = Integer.MAX_VALUE;
        for (Integer i : valuesZ) min = min < i ? min : i;
        return min;
    }

    public Integer findMax() {
        int max = Integer.MIN_VALUE;
        for (Integer i : valuesZ) max = max > i ? max : i;
        return max;
    }

    public boolean containsValueZ(Integer value) {
        return valuesZ.contains(value);
    }

    public boolean containsValueY(Integer value) {
        return valuesY.contains(value);
    }

    public List<Double> gradients() {
        List<Double> gradients = new ArrayList<Double>();

        int valueBefore = valuesZ.get(0);

        for (Integer currentValue :  valuesZ) {
            if (currentValue != valueBefore) {
                gradients.add(round(((100 / Double.valueOf(valueBefore)) * currentValue) - 100.0, 2));
            }
        }
        return gradients;
    }

    @Override
    public String toString() {
        return "XYGraph{" +
                "row=" + row +
                ", valuesZ=" + valuesZ +
                "}";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
