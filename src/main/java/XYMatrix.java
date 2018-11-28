import java.util.*;

/**
 * Stellt eine 2D-Matrix da.
 * x-Punkte und y-Punkte starten bei 1.
 */
public class XYMatrix {

    /**
     * Die Reichweite, in der eine neue Matrix beim Suchen um ein Punkt aufgebaut wird, wenn getHills aufgerufen wird.
     */
    private static int searchRange = 10;

    /**
     * Die Map stellt die Matrix da. Der Key sind die x-Punkte und die y-Punkte als Liste.
     * Der Index der Liste sind die y-Punkte und die Werte der Liste die z-Werte.
     */
    private Map<Integer, List<Integer>> values = new HashMap();
    /**
     * Da das Aufbauen einer großen Matrix lange dauert und das rotieren ebenfalls, wird die rotierte Matrix,
     * sie einmal rotiert wurde, abgespeichert.
     */
    private XYMatrix rotated = null;

    /**
     * Zum einfügen von einem x, y Punkt und dem z-Wert.
     * @param x - Der x-Punkt
     * @param value - Der Wert, der eingefügt werden soll.
     * @return - den y-Punkt
     */
    public int put(int x, int value) {
        if (values.containsKey(x)) { //Wenn es schon ein Key x gibt, dann muss die value-Liste nur erweitert werden.
            values.get(x).add(value);
        } else { //Ansonsten wird eine neue Liste angelegt und der Wert als erster Eintrag hinzugefügt
            values.put(x, new ArrayList<Integer>());
            values.get(x).add(value);
        }
        return values.get(x).indexOf(value);
    }

    /**
     * Gibt den Wert an einem Punkt x,y zurück.
     * @param x - x-Punkt
     * @param y - y-Punkt
     * @return - den z-Wert. Wenn kein Wert gefunden werden konnte, wird 0 zurückgegeben.
     */
    public int get(int x, int y) {
        if (values.containsKey(x)) {
            if (values.get(x).size() > y - 1) { //y-1 weil die indexierung einer Liste mit 0 beginnt und y mit 1;
                return values.get(x).get(y - 1);
            }
        }
        return 0;
    }

    /**
     * Rotiert die Matrix.
     * @return - die rotierte Matrix.
     */
    public XYMatrix rotate() {
        if (rotated != null) return rotated; //Falls bereits rotiert wurde, gebe die gespeicherte rotierte Matrix zurück
        XYMatrix xyMatrix = new XYMatrix();
        for (int i = 0; i < values.get(1).size(); i++) {
            for (int y = 1; y <= values.size(); y++) {
                xyMatrix.put(i+1, values.get(y).get(i));
            }
        }
        rotated = xyMatrix;
        return xyMatrix;
    }

    /**
     * Schneidet einen Ausschnitt von der Matrix von einem bestimmten Punkt aus mit einer bestimmten Reichweite.
     * Sollte die Reichweite außerhalb der Matrix liegen, wird kein Wert hinzugefügt.
     * @param x - x-Punkt
     * @param y - y-Punkt
     * @param range - Die Reichweite, mit der die Matrix aufgebaut werden soll
     * @return - Den Ausschnit der Matrix als Matrix.
     */
    public XYMatrix snipMatrix(Integer x, Integer y, Integer range) {
        XYMatrix snippetMatrix = new XYMatrix();
        for (int i = x - range ; i <= x + range ; i++) {
            for (int j = y - range; j < y + range + 1; j++) {
                if (i >= 0 && (j-1) >= 0) {
                   //Wenn kein Wert gefunden werden konnte, (get liefert 0), dann füge keinen werde zu der neuen Matrix hinzu.
                    if (this.get(i, j) != 0) {
                        snippetMatrix.put(i, get(i, j));
                    }
                }
            }
        }
        return snippetMatrix;
    }

    /**
     * Findet an einer an Punkt x,y mit einer Reichweite aufgebauten Matrix, den höchsten Wert.
     * @param x - x-Punkt
     * @param y - y-Punkt
     * @param range - Reichweite
     * @return - die Koordinaten des höchstens Punktes als Array der Größe 2. Gibt {0,0} zurück, wenn er keinen finden konnte.
     */
    public Integer[] findXYMaxinRange(Integer x, Integer y, Integer range) {
        Integer max = snipMatrix(x, y, range).findMaxValue();
        for (int i = x - range; i <= x + range; i++) {
            for (int j = y - range; j < y + range + 1; j++) {
                if (i >= 0 && (j - 1) >= 0) {
                    if (this.get(i, j) == max) {
                        return new Integer[]{i, j};
                    }
                }
            }
        }
        return new Integer[]{0,0};
    }

    /**
     * Findet den maximalen Wert in einer Matrix.
     * @return - den maximalen Wert
     */
    public Integer findMaxValue() {
        Integer max = -Integer.MAX_VALUE;
        for (Map.Entry<Integer, List<Integer>> entry : values.entrySet()) {
            for (Integer i : entry.getValue()) {
                max = i > max ? i : max;
            }
        }
        return max;
    }

    /**
     * Erstellt einen XYGraph, der an Punkt x,y liegt.
     * 
     * @param x
     * @param y
     * @return
     */
    public XYGraph findGraphInRow(Integer x, Integer y) {
        int begin = y;
        int end = y;
        XYGraph graph = new XYGraph(y);
        while (begin-1 > 1 && get(x,begin) > get(x, begin-1) ) {
            begin--;
        }
        while (get(x,end) > get(x,end+1)) {
            end++;
        }
        for (int i = begin ; i <= end ; i++ ) {

            graph.addValueY(get(x, i));
        }
        return graph;

    }

    public XYHill findHill(Integer x, Integer y) {
        return new XYHill(findGraphInRow(x,y), rotate().findGraphInRow(y,x), x, y );
    }

    public List<XYHill> getHills(List<XYPoint> points) {
        List<XYHill> hills = new ArrayList<XYHill>();

        for (XYPoint p : points) {
            Integer[] maximumPoints = findXYMaxinRange(p.x, p.y, searchRange);
            Integer x = maximumPoints[0];
            Integer y = maximumPoints[1];
            XYHill hill = findHill(x, y);
            hills.add(hill);
        }
        return hills;
    }

    public String toString() {
        String ret = "";

        for (int i = 0 ;  i < Main.datasize ; i++ ) {
            if (values.containsKey(i)) {
                ret+= i + ": ";
                for (Integer e : values.get(i)) {
                    ret += e + "\t";
                }
                ret+="\n";
            }
        }
        return ret;

    }

}
