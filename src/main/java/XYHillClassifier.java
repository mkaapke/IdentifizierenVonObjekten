import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import sun.awt.Symbol;

import javax.sound.midi.SysexMessage;

public class XYHillClassifier {

    public static int range = 10;


    public static double attributeFlatnessDef = 0.2;
    public static int triggerFlatness = 50;

    private static int[] attributeGradientRange = {-30, 30};


    private static int maxPointRange = 100; //vorsichtig

    private XYMatrix xyMatrix;

    public boolean isBObject(XYHill hill) {
        return true;
    }

    public XYHillClassifier(XYMatrix xyMatrix) {
        this.xyMatrix = xyMatrix;
    }

    public Integer isAObject(XYHill hill) {

        List<Double> left = hill.getxValues().gradients();
        List<Double> right = hill.getyValues().gradients();

        boolean print = false;

        //System.out.println(hill + "\n");
        return flatness(hill) ? 1 : 0;
    }

    public boolean flatness(XYHill hill) {
        Integer flatness = 0;

        for (Double d : hill.getxValues().gradients()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) {
            flatness++;
        }

        for (Double d : hill.getyValues().gradients()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;

        System.out.println((100 / (hill.getxValues().gradients().size() + hill.getyValues().gradients().size())) * flatness);

        return triggerFlatness > (100 / (hill.getxValues().gradients().size() + hill.getyValues().gradients().size())) * flatness ;
    }

    public boolean toSharp(XYHill hill) {
        for (Double d : hill.getxValues().gradients()) if (d > 6 || d < -6) return true;
        for (Double d : hill.getyValues().gradients()) if (d > 6 || d < -6) return true;
        return false;
    }


    public boolean isMaxInRange(XYHill hill) {
        XYMatrix snipped = xyMatrix.snipMatrix(hill.getX(), hill.getY(), maxPointRange);
        if (hill.getxValues().findMax() < snipped.findMaxValueXY()) return false;
        return true;
    }

    public double proportionOfAObject(List<XYHill> aHills) {
        Double amount = 0.0;
        //for (XYHill h : aHills) if (!h.isType()) amount++;
        return (100.0 / Double.valueOf(aHills.size())) * amount;
    }

    public double proportionOfBObject(List<XYHill> bHills) {
        Double amount = 0.0;
        //for (XYHill h : bHills) if (h.isType()) amount++;
        return (100.0 / Double.valueOf(bHills.size())) * amount;
    }
}
