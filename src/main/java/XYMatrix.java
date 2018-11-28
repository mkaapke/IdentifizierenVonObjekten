import java.util.*;

public class XYMatrix {

    private static int searchRange = 10;

    private Map<Integer, List<Integer>> values = new HashMap();
    private XYMatrix rotated = null;

    public int put(int x, int value) {
        if (values.containsKey(x)) {
            values.get(x).add(value);
        } else {
            values.put(x, new ArrayList<Integer>());
            values.get(x).add(value);
        }
        return values.get(x).indexOf(value);
    }

    public int get(int x, int y) {
        if (values.containsKey(x)) {
            if (values.get(x).size() > y - 1) {
                return values.get(x).get(y - 1);
            }
        }
        return 0;
    }

    public XYMatrix rotate() {
        if (rotated != null) return rotated;
        XYMatrix xyMatrix = new XYMatrix();
        for (int i = 0; i < values.get(1).size(); i++) {
            for (int y = 1; y <= values.size(); y++) {
                xyMatrix.put(i+1, values.get(y).get(i));
            }
        }
        rotated = xyMatrix;
        return xyMatrix;
    }

    public XYMatrix snipMatrix(Integer x, Integer y, Integer range) {
        XYMatrix snippetMatrix = new XYMatrix();
        for (int i = x - range ; i <= x + range ; i++) {
            for (int j = y - range; j < y + range + 1; j++) {
                if (i >= 0 && (j-1) >= 0) {
                    if (this.get(i, j) != 0) { //ACHTUNG
                        snippetMatrix.put(i, get(i, j));
                    }
                }
            }
        }
        return snippetMatrix;
    }

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

    public Integer findMaxValue() {
        Integer max = -Integer.MAX_VALUE;
        for (Map.Entry<Integer, List<Integer>> entry : values.entrySet()) {
            for (Integer i : entry.getValue()) {
                max = i > max ? i : max;
            }
        }
        return max;
    }

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

            graph.addValueZ(get(x, i));
        }
        return graph;

    }

    public XYHill findHill(Integer x, Integer y) {
        return new XYHill(findGraphInRow(x,y), rotate().findGraphInRow(y,x), x, y );
    }

    public List<XYHill> getHills(List<XYPoint> points) {
        List<XYHill> hills = new ArrayList<XYHill>();

        for (XYPoint p : points) {
            Integer[] maximumPoints = findXYMaxinRange(p.getX(), p.getY(), searchRange);
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
