import java.util.ArrayList;
import java.util.List;

/**
 * Stellt einen Graphen da. Besteht aus einem X-Wert und einer Liste aus Y-Werten.
 */
public class XYGraph {

    private Integer x;
    private List<Integer> valuesY = new ArrayList<Integer>();

    public XYGraph(Integer x) {
        this.x = x;
    }

    /**
     * Wird verwendet, um einen Y-Wert in den Graph einzufügen
     * @param value - der einzufügende Wert
     */
    public void addValueY(Integer value) {
        valuesY.add(value);
    }

    /**
     * Sucht den maximalen Wert im Graphen.
     * @return - maximalen Wert im Graphen
     */
    public Integer findMax() {
        int max = Integer.MIN_VALUE;
        for (Integer i : valuesY) max = max > i ? max : i;
        return max;
    }

    /**
     * Berechnet die Veränderungen in Prozent von einem Punkt zum nächsten Punkt und erstellt daraus eine Liste.
     * @return - Eine Liste mit der Veränderung in Prozent von Punkt x zu Punkt x + 1
     */
    public List<Double> gradientsPercent() {
        List<Double> gradients = new ArrayList<>();

        if (valuesY.isEmpty()) return gradients; //Wenn die Liste leer ist, dann wird eine leere Liste zurückgegeben.

        int valueBefore = valuesY.get(0);

        for (Integer currentValue : valuesY) {
            if (currentValue != valueBefore) { //Überspringe den ersten Durchlauf.
                gradients.add(Main.round(((100 / Double.valueOf(valueBefore)) * currentValue) - 100.0, 2)); //Wird auf 2 Nachkommastellen abgerundet.
            }
            valueBefore = currentValue;
        }
        return gradients;
    }

    /**
     * Berechnet die Veränderungen in Int-Werten von einem Punkt zum nächsten Punkt und erstellt daraus eine Liste.
     * @return - Eine Liste mit der Veränderung in Int-Werten von Punkt x zu Punkt x + 1
     */
    public List<Integer> gradientsInt() {
        List<Integer> gradients = new ArrayList<Integer>();

        if (valuesY.isEmpty()) return gradients; //Wenn die Liste leer ist, dann wird eine leere Liste zurückgegeben.

        int valueBefore = valuesY.get(0);

        for (Integer currentValue : valuesY) {
            if (currentValue != valueBefore) { //Überspringe den ersten Durchlauf.
                gradients.add(valueBefore - currentValue);
            }
            valueBefore = currentValue;
        }
        return gradients;
    }

    /**
     * Gibt den Teilgraphen zurück, der links vom maximalen Wert liegt.
     * @return
     */
    public XYGraph downGraph() {
        XYGraph graph = new XYGraph(this.x);
        Integer max = this.findMax();

        for (int i = valuesY.indexOf(max); i < valuesY.size() && i != -1 ; i++) {
            graph.addValueY(valuesY.get(i));
        }

        return graph;
    }

    /**
     * Gibt den Teilgraphen zurück, der rechts vom maximalen Wert liegt.
     * @return
     */
    public XYGraph upGraph() {
        XYGraph graph = new XYGraph(this.x);
        Integer max = this.findMax();

        for (int i = 0; i < valuesY.indexOf(max); i++) {
            graph.addValueY(valuesY.get(i));
        }

        return graph;
    }

    /**
     * Gibt den Graphen als String aus.
     * @return - Graphen als String.
     */
    public String toString() {
        return "XYGraph{" +
                "x=" + x +
                ", valuesY=" + valuesY +
                "}";
    }
}
