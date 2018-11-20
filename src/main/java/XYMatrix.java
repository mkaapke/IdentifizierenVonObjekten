import java.util.*;

public class XYMatrix {

    private Map<Integer, List<Integer>> values = new HashMap();

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
            if (values.get(x).size() > y) {
                return values.get(x).get(y);
            }
        }
        return 0;
    }

    public boolean containsXY(int x, int y) {
        return values.containsKey(x) ? values.get(x).size() > y : false;
    }

    public boolean remove(int x, int y) {
        if (values.containsKey(x)) {
            if (values.get(x).size() > y) {
                values.get(x).remove(y);
                return true;
            }
        }
        return false;
    }

    public Set<Map.Entry<Integer, List<Integer>>> entrySet() {
        return values.entrySet();
    }

    public XYMatrix rotate() {
        XYMatrix xyMatrix = new XYMatrix();
        for (int i = 0 ; i < values.get(0).size() ; i++) {
            for (int y = 0 ; y < values.size() ; y++) {
                xyMatrix.put(i, values.get(y).get(i));
            }
        }
        return xyMatrix;
    }

    public XYMatrix snipMatrix(Integer x, Integer y, Integer range) {
        XYMatrix snippetMatrix = new XYMatrix();

        for (int i = -range-1 ; i < range ; i++ ) {
            for (int j = -range-1; j < range; j++) {
                if (x + i >= 0 && y + j >= 0) {
                    snippetMatrix.put(x + i, this.get(x + i, y + j));
                }
            }
        }
        return snippetMatrix;
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

    public Integer[] findXYMaxinRange(Integer x, Integer y, Integer range) {
        Integer max = snipMatrix(x, y, range).findMaxValue();
        for (int i = -range-1 ; i < range ; i++ ) {
            for (int j = -range-1; j < range; j++) {
                if (this.get(x+i, y+j) == max) {
                    return new Integer[]{x+i+range, y+j+range};
                }

            }
        }
        return new Integer[]{0, 0};
    }



    @Override
    public String toString() {
        String ret = "";
        for (Map.Entry<Integer, List<Integer>> entry : values.entrySet()) {
            for (Integer e : entry.getValue()) {
                ret+= e + "\t";
            }
            ret+= "\n";
        }
        return ret;
    }

}
