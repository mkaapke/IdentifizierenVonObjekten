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

        /*Integer range = hill.getxValues().rangeGraphFromTop(10).upGraph().getSize() > hill.getxValues().rangeGraphFromTop(10).downGraph().getSize() ? hill.getxValues().rangeGraphFromTop(10).downGraph().getSize() : hill.getxValues().rangeGraphFromTop(10).upGraph().getSize();
        List<Integer> left = hill.getxValues().rangeGraphFromTop(10).upGraph().getValuesZ();
        Collections.reverse(left);
        List<Integer> right = hill.getxValues().rangeGraphFromTop(10).downGraph().getValuesZ();

        int wert = 0;

        System.out.println(left);
        System.out.println(right);

        for (int i = 0 ;  i < range ; i++) {
            double prozent = left.get(i).doubleValue() / right.get(i).doubleValue();
            if (prozent > -0.99 && prozent < 1.1) wert++;
            System.out.println(prozent);
        }*/

        /*for (Double d : hill.getxValues().gradients()) {
            if (d > 5 || d < -5) return 0;
        }

        for (Double d : hill.getyValues().gradients()) {
            if (d > 5 || d < -5) return 0;
        }*/

        System.out.println(hill);

        return flatness(hill) ? 0 : 1;
    }

    public boolean flatness(XYHill hill) {
        Integer flatness = 0;

        for (Double d : hill.getxValues().gradients()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) {
            flatness++;
        }

        for (Double d : hill.getyValues().gradients()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;

        //System.out.println((100 / (hill.getxValues().gradients().size() + hill.getyValues().gradients().size())) * flatness);

        return ((100 / (hill.getxValues().gradients().size() + hill.getyValues().gradients().size())) * flatness) > 50 ;
    }

    public boolean evenlyRise(XYHill hill) {
        List<Double> left = hill.getxValues().snipGraphFromTop(50).upGraph().gradients();
        List<Double> right = hill.getxValues().snipGraphFromTop(50).downGraph().gradients();
        double leftdss = 0;
        double rightdss = 0;
        boolean hitX = false;
        boolean hitY = false;


        for (Double d : left) { leftdss += d; }
        for (Double d : right) { rightdss += d; }
        if (((100 / (leftdss / left.size()) * (rightdss / -right.size())) - 100) < 100 && ((100 / (leftdss / left.size()) * (rightdss / -right.size())) - 100) > -100)
            hitX = true;

        left = hill.getyValues().snipGraphFromTop(50).upGraph().gradients();
        right = hill.getyValues().snipGraphFromTop(50).downGraph().gradients();
        leftdss = 0;
        rightdss = 0;

        for (Double d : left) { leftdss += d; }
        for (Double d : right) { rightdss += d; }
        if (((100 / (leftdss / left.size()) * (rightdss / -right.size())) - 100) < 100 && ((100 / (leftdss / left.size()) * (rightdss / -right.size())) - 100) > -100)
            hitY = true;

        return  hitX && hitY;

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
