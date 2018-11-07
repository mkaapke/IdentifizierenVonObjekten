import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XYMatrix {

    private Map<Integer, List<Integer>> values = new HashMap();

    public int put(int x, int value) {
        if (values.containsKey(x)) {
            values.get(x).add(value);
        } else {
            values.put(x, new ArrayList<Integer>());
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

    @Override
    public String toString() {
        return "XYMatrix{" +
                "values=" + values +
                '}';
    }
}
