import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XYHillClassifier {

    private static final double attributeFlatnessDef = 0.2;
    private static final int triggerFlatnessPercent = 50;

    private static final double[] attributenotSymetricDef = {0,9, 1.1};
    private static final int triggerSymetricPercent = 20;

    private static final int attributeSharpnessDef = 3;
    private static final int attributeSharpnessRange = 3;

    private static final double pASym = 0.857;
    private static final double pAsharp = 0.3075;
    private static final double pAflat = 0.125;

    private static final double pBSym = 0.44;
    private static final double pBsharp = 0.535;
    private static final double pBflat = 0.4425;

    private double pAHill;
    private double pBHill;
    private double amountAHills;
    private double amountBHills;

    public XYHillClassifier(double amountAHills, double amountBHills) {
        this.amountAHills = amountAHills;
        this.amountBHills = amountBHills;
        this.pAHill = amountAHills / (amountAHills + amountBHills);
        this.pBHill = amountBHills / (amountAHills + amountBHills);;
    }

    public List<XYHill> findAObjects(List<XYHill> hills) {
        List<XYHill> aHills = new ArrayList<XYHill>();
        int counter = 0;

        for (XYHill hill : hills) {
            double isAHill = 1.0;
            double isBHill = 1.0;
            double isFlatA = flat(hill) ? (pAflat * pAHill) / pAHill : 1;
            double isSharpA = sharp(hill) ? (pAsharp * pAHill) / pAHill : 1;
            double isSymA = isSymetric(hill) ? (pASym * pAHill) / pAHill : 1;

            double isFlatB = flat(hill) ? (pBflat * pBHill) / pBHill : 1;
            double isSharpB = sharp(hill) ? (pBsharp * pBHill) / pBHill : 1;
            double isSymB = isSymetric(hill) ? (pBSym * pBHill) / pBHill : 1;

            isAHill = isFlatA * isSharpA * isSymA * pAHill;
            isBHill = isFlatB * isSharpB * isSymB * pBHill;

            if ((isAHill < isBHill)) counter++;
        }

        System.out.println(counter);

        return aHills;
    }

    public boolean isSymetric(XYHill hill) {
        List<Integer> left = hill.getxValues().upGraph().gradientsInt();
        Collections.reverse(left);
        List<Integer> right = hill.getxValues().downGraph().gradientsInt();
        Integer rangeX = left.size() > right.size() ? right.size() : left.size();
        double wertX = 0;
        for (int i = 1 ;  i < rangeX ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();
            if (prozent < attributenotSymetricDef[1] && prozent > attributenotSymetricDef[0]) wertX++; else wertX--;
        }
        double symetricProportionX = wertX > 0 ? (100 / rangeX) * wertX : 0;
        if (wertX < 5) return false;
        if (symetricProportionX < triggerSymetricPercent) return false;

        left = hill.getyValues().upGraph().gradientsInt();
        Collections.reverse(left);
        right = hill.getyValues().downGraph().gradientsInt();
        Integer rangeY = left.size() > right.size() ? right.size() : left.size();
        double wertY = 0;
        for (int i = 1 ;  i < rangeY ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();
            if (prozent < attributenotSymetricDef[1] && prozent > attributenotSymetricDef[0]) wertY++; else wertY--;
        }
        double symetricProportionY = wertY > 0 ? (100 / rangeY) * wertY : 0;
        if (wertY < 5) return false;
        if (symetricProportionY < triggerSymetricPercent) return false;

        return true;
    }


    public boolean flat(XYHill hill) {
        Integer flatness = 0;
        for (Double d : hill.getxValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        for (Double d : hill.getyValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        return ((100 / (hill.getxValues().gradientsPercent().size() + hill.getyValues().gradientsPercent().size())) * flatness) > triggerFlatnessPercent ;
    }

    public boolean sharp(XYHill hill) {

        for (int i = 0 ; i < attributeSharpnessRange ; i++) {
            if (hill.getxValues().gradientsPercent().get(i) < -attributeSharpnessDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) < -attributeSharpnessDef) return true;
            if (hill.getyValues().gradientsPercent().get(i) < -attributeSharpnessDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) < -attributeSharpnessDef) return true;
            if (hill.getxValues().gradientsPercent().get(i) > attributeSharpnessDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) > attributeSharpnessDef) return true;
            if (hill.getyValues().gradientsPercent().get(i) > attributeSharpnessDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) > attributeSharpnessDef) return true;
        }

        return false;
    }

    public double anzObjektFlat(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.flat(h)) counter++;
        }
        return counter;
    }

    public double anzObjektSym(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.isSymetric(h)) counter++;
        }
        return counter;
    }

    public double anzObjektSharp(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.sharp(h)) counter++;
        }
        return counter;
    }

}
