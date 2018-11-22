import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import javax.sound.midi.SysexMessage;

public class XYHillClassifier {

    public static int range = 10;

    public static double attributeDropAverage = 0.;

    public static double attributeFlatness = 0.2;

    public boolean isBObject(XYHill hill) {

        return true;
    }


    public boolean isAObject(XYHill hill) {

        System.out.println(flatness(hill));

        /*System.out.println(hill.getxValues());
        System.out.println(hill.getyValues());

        Integer before = hill.getxValues().getValuesZ().get(0);
        Double dss = 0.0;
        for (Double d : hill.getxValues().gradients()) {
            dss+= d;
            System.out.print(d + " = ");
        }
        System.out.println(dss / hill.getxValues().gradients().size());
        dss = 0.0;
        for (Double d : hill.getyValues().gradients()) {
            dss+= d;
            System.out.print(d + " = ");
        }
        System.out.println(dss / hill.getxValues().gradients().size() + "\n");*/


        //System.out.println(hill.getxValues().gradients());
        //System.out.println(hill.getyValues().gradients() + "\n");
        /*SimpleRegression simpleRegressionXUp = new SimpleRegression(true);
        SimpleRegression simpleRegressionXDown = new SimpleRegression(true);
        SimpleRegression simpleRegressionYUp = new SimpleRegression(true);
        SimpleRegression simpleRegressionYDown = new SimpleRegression(true);
        int i;

        for (i = range ; i < hill.getxValues().getValuesZ().size()-range ; i++) {
            simpleRegressionXUp.addData(i, hill.getxValues().getValuesZ().get(i));
            if (hill.getxValues().getValuesZ().get(i).equals(hill.getxValues().findMax())) break;
        }

        for (i = i ; i < hill.getxValues().getValuesZ().size()-range ; i++) {
            simpleRegressionXDown.addData(i, hill.getxValues().getValuesZ().get(i));
        }

        for (i = range ; i < hill.getyValues().getValuesZ().size()-range ; i++) {
            simpleRegressionYUp.addData(i, hill.getyValues().getValuesZ().get(i));
            if (hill.getyValues().getValuesZ().get(i).equals(hill.getyValues().findMax())) break;
        }

        for (i = i ; i < hill.getyValues().getValuesZ().size()-range ; i++) {
            simpleRegressionYDown.addData(i, hill.getyValues().getValuesZ().get(i));
        }

        System.out.println(hill);
        System.out.println(simpleRegressionXUp.);
        System.out.println(simpleRegressionXDown.getRSquare());
        System.out.println(simpleRegressionYUp.getRSquare());
        System.out.println(simpleRegressionYDown.getRSquare() + "\n");*/

        return false;
    }

    public void distance(XYGraph graphUp, XYGraph graphDown) {
        Integer boarder = graphDown.getSize() > graphUp.getSize() ? graphUp.getSize()-1 : graphDown.getSize()-1;

        if (graphDown.getSize() > graphUp.getSize()) {
            int y = 0;
            for (int i = boarder ; i >= 0 ; i--) {
                System.out.println(graphUp.getValuesZ().get(i) - graphDown.getValuesZ().get(y++));
            }
        } else {
            int i = boarder;
            for (int y = 0 ; y < graphDown.getSize() ; y++) {
                System.out.println(graphUp.getValuesZ().get(i--) - graphDown.getValuesZ().get(y));
            }
        }
    }

    public Integer toGreatDrop(XYHill h) {
        Integer point = 0;
        for (Double d : h.getxValues().gradients()) {
            if (d > attributeDropAverage) point++ ;
        }
        for (Double d : h.getxValues().gradients()) {
            if (d > attributeDropAverage) point++ ;
        }
        return 0;
    }

    public Integer flatness(XYHill hill) {
        Integer flatness = 0;

        for (Double d : hill.getxValues().gradients()) if (d < attributeFlatness &&  d > -attributeFlatness) flatness++;

        for (Double d : hill.getyValues().gradients()) if (d < attributeFlatness &&  d > -attributeFlatness) flatness++;

        return flatness;
    }

    public double proportionOfAObject(List<XYHill> aHills) {
        Double amount = 0.0;
        for (XYHill h : aHills) if (!h.isType()) amount++;
        return (100.0 / Double.valueOf(aHills.size())) * amount;
    }

    public double proportionOfBObject(List<XYHill> bHills) {
        Double amount = 0.0;
        for (XYHill h : bHills) if (h.isType()) amount++;
        return (100.0 / Double.valueOf(bHills.size())) * amount;
    }
}
