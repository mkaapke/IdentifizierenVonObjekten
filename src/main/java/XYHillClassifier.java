import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XYHillClassifier {

    private static final double flatnessDef = 0.2;
    private static final int flatnessPercent = 50;

    private static final double[] symetricRange = {0,9, 1.1};
    private static final int triggerSymetricPercent = 20;

    private static final int abruptRise = 3;
    private static final int abruptRiseRange = 3;

    private static double pAsym;
    private static double pAabruptRise;
    private static double pAflat;

    private static double pBsym;
    private static double pBabruptRise;
    private static double pBflat;

    private boolean isTrained = false;


    public List<XYHill> findBObjects(List<XYHill> hills) {
        List<XYHill> bHills = new ArrayList<>();
        if (!isTrained) {
            System.out.println("Klassifikator ist nicht trainiert.");
            return bHills;
        }
        for (XYHill hill : hills) {
            double isAHill;
            double isBHill;

            double isFlatA = (isFlat(hill) ? pAflat : 1-pAflat);
            double isSharpA = (isSharp(hill) ? pAabruptRise : 1- pAabruptRise);
            double isSymA = (isSymetric(hill) ? pAsym : 1- pAsym);

            double isFlatB = (isFlat(hill) ? pBflat : 1-pBflat);
            double isSharpB = (isSharp(hill) ? pBabruptRise : 1- pBabruptRise);
            double isSymB = (isSymetric(hill) ? pBsym : 1- pBsym);

            isAHill = isFlatA * isSharpA * isSymA;
            isBHill = isFlatB * isSharpB * isSymB;
            //Wenn die Wahrscheinlichkeiten gleich sind, was selten vor kommt, ist es ein BObjekt
            if ((isAHill <= isBHill)) bHills.add(hill);
        }

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
            if (prozent < symetricRange[1] && prozent > symetricRange[0]) wertX++; else wertX--;
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
            if (prozent < symetricRange[1] && prozent > symetricRange[0]) wertY++; else wertY--;
        }
        double symetricProportionY = wertY > 0 ? (100 / rangeY) * wertY : 0;
        if (wertY < 5) return false;
        if (symetricProportionY < triggerSymetricPercent) return false;
        return true;
    }

    private boolean isFlat(XYHill hill) {
        Integer flatness = 0;
        for (Double d : hill.getxValues().gradientsPercent()) if (d < flatnessDef &&  d > -flatnessDef) flatness++;
        for (Double d : hill.getyValues().gradientsPercent()) if (d < flatnessDef &&  d > -flatnessDef) flatness++;
        return ((100 / (hill.getxValues().gradientsPercent().size() + hill.getyValues().gradientsPercent().size())) * flatness) > flatnessPercent;
    }

    private boolean isSharp(XYHill hill) {
        for (int i = 0; i < abruptRiseRange; i++) {
            if (hill.getxValues().gradientsPercent().get(i) < -abruptRise || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) < -abruptRise) return true;
            if (hill.getyValues().gradientsPercent().get(i) < -abruptRise || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) < -abruptRise) return true;
            if (hill.getxValues().gradientsPercent().get(i) > abruptRise || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) > abruptRise) return true;
            if (hill.getyValues().gradientsPercent().get(i) > abruptRise || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) > abruptRise) return true;
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
        System.out.println("---------AOBJEKTE: " + aHills.size() + "----------");
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

        double bListe = bHills.size();

        double flatB = anzObjektFlat(hills);
        double symB = anzObjektSym(hills);
        double sharpB = anzObjektSharp(hills);

        System.out.println("--Flat " + anzObjektFlat(hills));
        System.out.println("--Sym " + anzObjektSym(hills));
        System.out.println("--Sharp " + anzObjektSharp(hills));

        System.out.println("------Bayes-----");

        pAsym = symA / aListe;
        pAabruptRise = sharpA / aListe;
        pAflat = flatA / aListe;

        pBsym = symB / bListe;
        pBabruptRise = sharpB / bListe;
        pBflat = flatB / bListe;

        System.out.println("-----Wahrscheinlichkeiten A -----");
        System.out.println(("P(Sym|A) = " + pAsym));
        System.out.println(("P(Sharp|A) = " + pAabruptRise));
        System.out.println(("P(Flat|A) = " + pAflat));
        System.out.println("-----Wahrscheinlichkeiten B -----");
        System.out.println(("P(Sym|B) = " + pBsym));
        System.out.println(("P(Sharp|B) = " + pBabruptRise));
        System.out.println(("P(Flat|B) = " + pBflat));

        isTrained = true;
    }

}
