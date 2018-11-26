import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;

import javax.sound.midi.SysexMessage;
import java.util.Collections;
import java.util.List;

public class XYHillClassifier {


    public static final double  attributeFlatnessDef = 0.2;
    public static final int triggerFlatness = 50;

    public static final double[] notSymetric = {1.5, 0.5};


    private XYMatrix xyMatrix;

    public boolean isBObject(XYHill hill) {
        return true;
    }

    public XYHillClassifier(XYMatrix xyMatrix) {
        this.xyMatrix = xyMatrix;
    }

    public boolean isAObject(XYHill hill) {
        return !flatness(hill);
    }

    public boolean isSymetric(XYHill hill) {
        List<Integer> left = hill.getxValues().upGraph().gradientsInt();
        Collections.reverse(left);
        List<Integer> right = hill.getxValues().downGraph().gradientsInt();
        Integer range = left.size() > right.size() ? right.size() : left.size();
        double wert = 0;
        for (int i = 0 ;  i < range ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();
            if (prozent < 1.5 && prozent > 0.5) wert++;
        }
        return ((100 / range) * wert) < 30;
    }

    public boolean flatness(XYHill hill) {
        Integer flatness = 0;
        for (Double d : hill.getxValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        for (Double d : hill.getyValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        return ((100 / (hill.getxValues().gradientsPercent().size() + hill.getyValues().gradientsPercent().size())) * flatness) > triggerFlatness ;
    }


    public boolean toSharp(XYHill hill) {
        for (Double d : hill.getxValues().gradientsPercent()) if (d > 6 || d < -6) return true;
        for (Double d : hill.getyValues().gradientsPercent()) if (d > 6 || d < -6) return true;
        return false;
    }

}
