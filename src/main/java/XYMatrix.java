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
//
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

    public Map<Integer, List<Integer>> getValues() {
        return values;
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
