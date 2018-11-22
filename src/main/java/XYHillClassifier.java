import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import javax.sound.midi.SysexMessage;

public class XYHillClassifier {

    public static int range = 10;

    public static double attributeDropAverage = 0.;

    public static int triggerFlatness = 50;
    public static double attributeFlatnessDef = 0.2;
    public static int weightFlatness = 10;

    public boolean isBObject(XYHill hill) {
        return true;
    }


    public Integer isAObject(XYHill hill) {

        System.out.println(hill.getxValues());
        System.out.println(hill.getxValues().gradients());
        System.out.println(hill.getyValues());
        System.out.println(hill.getyValues().gradients() + "\n");

        return flatness(hill) < 50 ? 0 : 1;
    }

    public Integer flatness(XYHill hill) {
        Integer flatness = 0;

        for (Double d : hill.getxValues().gradients()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;

        for (Double d : hill.getyValues().gradients()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;

        return (100 / (hill.getxValues().gradients().size() + hill.getyValues().gradients().size())) * flatness ;
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
