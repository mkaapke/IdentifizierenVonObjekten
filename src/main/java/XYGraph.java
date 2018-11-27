import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XYGraph {

    private Integer row;
    private List<Integer> valuesZ = new ArrayList<Integer>();

    public XYGraph(Integer row) {
        this.row = row;
    }

    public void addValueZ(Integer value) {
        valuesZ.add(value);
    }

    public Integer findMax() {
        int max = Integer.MIN_VALUE;
        for (Integer i : valuesZ) max = max > i ? max : i;
        return max;
    }

    public List<Double> gradientsPercent() {
        List<Double> gradients = new ArrayList<Double>();

        if (valuesZ.isEmpty()) return gradients;

        int valueBefore = valuesZ.get(0);

        for (Integer currentValue :  valuesZ) {
            if (currentValue != valueBefore) {
                gradients.add(round(((100 / Double.valueOf(valueBefore)) * currentValue) - 100.0, 2));
            }
            valueBefore = currentValue;
        }
        return gradients;
    }

    public List<Integer> gradientsInt() {
        List<Integer> gradients = new ArrayList<Integer>();

        if (valuesZ.isEmpty()) return gradients;

        int valueBefore = valuesZ.get(0);

        for (Integer currentValue :  valuesZ) {
            if (currentValue != valueBefore) {
                gradients.add(valueBefore - currentValue);
            }
            valueBefore = currentValue;
        }
        return gradients;
    }


    public String toString() {
        return "XYGraph{" +
                "row=" + row +
                ", valuesZ=" + valuesZ +
                "}";
    }

    public XYGraph downGraph() {
        XYGraph graph = new XYGraph(this.row);
        Integer max = this.findMax();

        for (int i = valuesZ.indexOf(max) ; i < valuesZ.size() && i != -1 ; i++) {
            graph.addValueZ(valuesZ.get(i));
        }

        return graph;
    }

    public XYGraph upGraph() {
        XYGraph graph = new XYGraph(this.row);
        Integer max = this.findMax();

        for (int i = 0 ; i < valuesZ.indexOf(max); i++) {
            graph.addValueZ(valuesZ.get(i));
        }

        return graph;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
