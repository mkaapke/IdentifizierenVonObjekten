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

    private double pAHill; //Wie genau sollen wir das berechnen?
    private double pBHill;

    public XYHillClassifier(double amountAHills, double amountBHills) {
        this.pAHill = amountAHills / (amountAHills + amountBHills);
        this.pBHill = amountBHills / (amountAHills + amountBHills);;
    }

    public List<XYHill> findBObjects(List<XYHill> hills) {
        List<XYHill> bHills = new ArrayList<>();

        for (XYHill hill : hills) { //Ist die erste Reihe der Matrix doppelt?
            double isAHill;
            double isBHill;
            double isFlatA = (isFlat(hill) ? (pAflat * pAHill) / pAHill : 0) + 0.00000001; //Dürfen wir das? //Muss die Gegenwahrscheinlichkeit gewählt werden?
            double isSharpA = (isSharp(hill) ? (pAsharp * pAHill) / pAHill : 0) + 0.00000001;
            double isSymA = (isSymetric(hill) ? (pASym * pAHill) / pAHill : 0) + 0.00000001;

            double isFlatB = (isFlat(hill) ? (pBflat * pBHill) / pBHill : 0) + 0.00000001;
            double isSharpB = (isSharp(hill) ? (pBsharp * pBHill) / pBHill : 0) + 0.00000001 ;
            double isSymB = (isSymetric(hill) ? (pBSym * pBHill) / pBHill : 0) + 0.00000001;

            isAHill = isFlatA * isSharpA * isSymA * pAHill;
            isBHill = isFlatB * isSharpB * isSymB * pBHill;

            //Was passiert, wenn die Wahrscheinlichkeiten gleich sind?
            //if (isAHill == isBHill) System.out.println(hill);

            if ((isAHill < isBHill)) bHills.add(hill); //< oder <=????
        }

        System.out.println(bHills.size());

        return bHills;
    }

    private boolean isSymetric(XYHill hill) {
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

    private boolean isFlat(XYHill hill) {
        Integer flatness = 0;
        for (Double d : hill.getxValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        for (Double d : hill.getyValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        return ((100 / (hill.getxValues().gradientsPercent().size() + hill.getyValues().gradientsPercent().size())) * flatness) > triggerFlatnessPercent ;
    }

    private boolean isSharp(XYHill hill) {

        for (int i = 0 ; i < attributeSharpnessRange ; i++) {
            if (hill.getxValues().gradientsPercent().get(i) < -attributeSharpnessDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) < -attributeSharpnessDef) return true;
            if (hill.getyValues().gradientsPercent().get(i) < -attributeSharpnessDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) < -attributeSharpnessDef) return true;
            if (hill.getxValues().gradientsPercent().get(i) > attributeSharpnessDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) > attributeSharpnessDef) return true;
            if (hill.getyValues().gradientsPercent().get(i) > attributeSharpnessDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) > attributeSharpnessDef) return true;
        }

        return false;
    }

    private double anzObjektFlat(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.isFlat(h)) counter++;
        }
        return counter;
    }

    private double anzObjektSym(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.isSymetric(h)) counter++;
        }
        return counter;
    }

    private double anzObjektSharp(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.isSharp(h)) counter++;
        }
        return counter;
    }

    public void training(List<XYHill> aHills, List<XYHill> bHills) {
        System.out.println("---------ABOJEKTE: " + aHills.size() + "----------");
        List<XYHill> hills = aHills;

        double aListe = aHills.size();

        double flatA = anzObjektFlat(hills);
        double symA = anzObjektSym(hills);
        double sharpA = anzObjektSharp(hills);

        System.out.println("--Flat " + anzObjektFlat(hills));
        System.out.println("--Sym " + anzObjektSym(hills));
        System.out.println("--Sharp " + anzObjektSharp(hills));

        System.out.println("---------BOBJEKTE: " + bHills.size() + "----------");
        hills = bHills;

        double bListe = aHills.size();

        double flatB = anzObjektFlat(hills);
        double symB = anzObjektSym(hills);
        double sharpB = anzObjektSharp(hills);

        System.out.println("--Flat " + anzObjektFlat(hills));
        System.out.println("--Sym " + anzObjektSym(hills));
        System.out.println("--Sharp " + anzObjektSharp(hills));

        System.out.println("------Bayes-----");

        double pAsym = symA / aListe;
        double pAsharp = sharpA / aListe;
        double pAflat = flatA / aListe;

        double pBsym = symB / bListe;
        double pBsharp = sharpB / bListe;
        double pBflat = flatB / bListe;

        System.out.println("-----Wahrscheinlichkeiten A -----");
        System.out.println(("P(Sym|A) = " + pAsym));
        System.out.println(("P(Sharp|A) = " + pAsharp));
        System.out.println(("P(Flat|A) = " + pAflat));
        System.out.println("-----Wahrscheinlichkeiten B -----");
        System.out.println(("P(Sym|B) = " + pBsym));
        System.out.println(("P(Sharp|B) = " + pBsharp));
        System.out.println(("P(Flat|B) = " + pBflat));

        double q = (pAsym * pAsharp * pAflat) / (pBsym * pBsharp * pBflat);

        System.out.println("---" + q);
    }

}
