import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XYHillClassifier {

    //-----Parameter für die Flachheit eines XYHill-----
    /**
     * Definiert, wann eine Steigung flach ist.
     */
    private static final double flatnessDef = 0.2;
    /**
     * Definiert, ab wie viele Prozent an flachen Punkten in einem XYHill, der XYHill flach ist.
     */
    private static final int flatnessPercent = 50;

    //-----Parameter für die Symmetrie eines XYHill-----
    /**
     * Definiert, wie groß die Abweichung zwischen zwei gegenüberliegenden Punkten, sein darf, damit sie als symmetrisch gelten.
     * Der erste Wert ist die untere Schranke und der zweite Wert die obere. Um symmetrisch zu sein, muss der Wert dazwischen liegen.
     */
    private static final double[] symetricRange = {0,9, 1.1};
    /**
     * Definiert, ab wie viele Prozent an symmetrischen Punkten in einem XYHill, der XYHill symmetrisch ist.
     */
    private static final int triggerSymetricPercent = 20;
    /**
     * Definiert, ab welcher Anzahl an symmetrischen Punkten in einem XYHill, der XYHill symmetrisch ist.
     */
    private static final int triggerSymetricInt = 5;

    //-----Parameter für die zu starke Steigung eines XYHill-----
    /**
     * Definiert, wie hoch die Steigung (in Prozent) sein darf, damit sie als stark steigend gilt.
     */
    private static final int abruptRise = 3;
    /**
     * Definiert, in welcher Reichweite jeweils am Anfang und Ende der XYGraphen für die x-Ansicht und y-Ansicht,
     * nach einem stark steigenden Wert gesucht wird.
     */
    private static final int abruptRiseRange = 3;

    //-----WAHRSCHEINLICHKEITEN-----
    private static double pAsym; //Wahrscheinlichkeit für die Symmetrie eines A-Objekt.
    private static double pAabruptRise; //Wahrscheinlichkeit für die stark steigende Steigung eines A-Objekt.
    private static double pAflat; //Wahrscheinlichkeit für die Flachheit eines A-Objekt.

    private static double pBsym; //Wahrscheinlichkeit für die Symmetrie eines B-Objekt.
    private static double pBabruptRise; //Wahrscheinlichkeit für die stark steigende Steigung eines B-Objekt.
    private static double pBflat; //Wahrscheinlichkeit für die Flachheit eines B-Objekt.

    private boolean isTrained = false; //Der Klassifikator merkt sich, ob die Daten trainiert wurden.

    /**
     * Sucht in einer Liste von XYHills nach A oder B-Objekten. Gefundene A oder B-Objekte werden in eine Liste gespeichert.
     * @param hills - Liste mit XYHills, in der gesucht werden soll.
     * @param objekt - setzte für A-Objekte auf true, für B-Objekte auf false.
     * @return - Die Liste mit den gefundenen B-Objekten. Sollte der Klassifikator nicht trainiert sein, wird eine
     * leere Liste zurückgegeben.
     */
    public List<XYHill> findABObjects(List<XYHill> hills, boolean objekt) {
        List<XYHill> classifiedHills = new ArrayList<>();
        if (!isTrained) { //Wenn der Klassifikator nicht trainiert wurde, wird die leere Liste zurückgegeben.
            System.out.println("Klassifikator ist nicht trainiert.");
            return classifiedHills;
        }
        for (XYHill hill : hills) {
            double isAHill; //Wahrscheinlichkeit für ein A-Objekt
            double isBHill; //Wahrscheinlichkeit für ein B-Objekt

            //Berechnung der Wahrscheinlichkeiten
            double isFlatA = (isFlat(hill) ? pAflat : 1-pAflat);
            double isSharpA = (hasAbruptRise(hill) ? pAabruptRise : 1- pAabruptRise);
            double isSymA = (isSymetric(hill) ? pAsym : 1- pAsym);

            double isFlatB = (isFlat(hill) ? pBflat : 1-pBflat);
            double isSharpB = (hasAbruptRise(hill) ? pBabruptRise : 1- pBabruptRise);
            double isSymB = (isSymetric(hill) ? pBsym : 1- pBsym);

            isAHill = isFlatA * isSharpA * isSymA;
            isBHill = isFlatB * isSharpB * isSymB;

            //Wenn die Wahrscheinlichkeiten gleich sind, wird es das Objekt als B-Objekt eingestuft,
            //weil es sehr unwahrscheinlich ist, dass die Wahrscheinlichkeiten gleich sind.
            
            if (objekt) {
                if ((isAHill > isBHill)) classifiedHills.add(hill);
            } else {
                if ((isAHill <= isBHill)) classifiedHills.add(hill);
            }
           
        }
        return classifiedHills;
    }

    /**
     * Prüft, ob ein XYHill Symmetrisch ist.
     * Als symmetrisch gilt er, wenn:
     * - Gegenüberliegende Punkte dürfen eine Abweichung enthalten, die zwischen den Schranken von symetricRange liegt,
     * - Es muss pro XYGraph eine Mindestanzahl an Punkten geben. Die Zahl wird von triggerSymetricInt definiert oder
     * - Ein prozentualer Anteil an symmetrischen Punkten muss pro XYGraph gegeben sein. Der geforderte Anteil wird von triggerSymetricPercent gegeben.
     * @param hill - Der XY-Hill, der untersucht wird.
     * @return - true, wenn symmetrisch, false wenn nicht.
     */
    private boolean isSymetric(XYHill hill) {
        List<Integer> left = hill.getxValues().upGraph().gradientsInt();        //Die linke Seite des x-XYGraph, exklusive des Höhepunktes.
        Collections.reverse(left);                                              //Diese muss gedreht werden, damit die get-Methode der linken Seite den höchsten Wert liefert.
        List<Integer> right = hill.getxValues().downGraph().gradientsInt();     //Die rechte Seite des x-XYGraph, exklusive des Höhepunktes.
        //Da left und right meistens nicht gleichlang sind, muss für die Iteration eine Grenze gewählt werden, die der kleinen Seite entspricht
        Integer rangeX = left.size() > right.size() ? right.size() : left.size();
        double wertX = 0; //Zähler für die Symmetrie
        for (int i = 0 ;  i < rangeX ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();           //Berechnung der Abweichung von den gegenüberliegenden Punkten
            if (prozent < symetricRange[1] && prozent > symetricRange[0]) wertX++; else wertX--;    //Liegt der Wert innerhalb der symetricRange, dann wird WertX inkrementiert, ansonsten dekrementiert.
        }
        double symetricProportionX = wertX > 0 ? (100 / rangeX) * wertX : 0;    //Berechnung der Anzahl an symmetrischen Punkten an der gesamten Anzahl der Punkten in Prozent
        //Wenn es weniger symmetrische Punkte gibt als triggerSemetricInt oder triggerSymetricPercent, gilt das Objekt als unsymmetrisch
        if (wertX < triggerSymetricInt || symetricProportionX < triggerSymetricPercent) return false;

        //Das gleiche wird mit dem y-XYGraph gemacht.
        left = hill.getyValues().upGraph().gradientsInt();
        Collections.reverse(left);
        right = hill.getyValues().downGraph().gradientsInt();
        Integer rangeY = left.size() > right.size() ? right.size() : left.size();
        double wertY = 0;
        for (int i = 0 ;  i < rangeY ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();
            if (prozent < symetricRange[1] && prozent > symetricRange[0]) wertY++; else wertY--;
        }
        double symetricProportionY = wertY > 0 ? (100 / rangeY) * wertY : 0;
        if (wertY < triggerSymetricInt || symetricProportionY < triggerSymetricPercent ) return false;
        return true;
    }

    /**
     * Prüft, ob ein XYHill flach ist.
     * Eine flache Steigung wird beschrieben durch den Parameter flatnessDef.
     * Als flach gilt er, wenn der Anteil an flachen Steigungen in Prozent, größer ist, als der Parameter flatnessPercent.
     * @param hill - Der XY-Hill, der untersucht wird.
     * @return - true, wenn symmetrisch, false wenn nicht.
     */
    private boolean isFlat(XYHill hill) {
        Integer flatness = 0;
        for (Double d : hill.getxValues().gradientsPercent()) if (d < flatnessDef &&  d > -flatnessDef) flatness++;
        for (Double d : hill.getyValues().gradientsPercent()) if (d < flatnessDef &&  d > -flatnessDef) flatness++;
        return ((100 / (hill.getxValues().gradientsPercent().size() + hill.getyValues().gradientsPercent().size())) * flatness) > flatnessPercent;
    }

    /**
     * Prüft, ob ein XYHill eine starke Steigung in einer bestimmten Reichweite, gegeben durch abruptRiseRange, existiert.
     * Eine starke Steigung wird definiert durch abruptRise.
     * @param hill
     * @return
     */
    private boolean hasAbruptRise(XYHill hill) {

        Integer range = abruptRiseRange > hill.getxValues().gradientsPercent().size() ? hill.getxValues().gradientsPercent().size() : abruptRiseRange;
        for (int i = 0; i < range; i++) {
            if (hill.getxValues().gradientsPercent().get(i) < -abruptRise || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) < -abruptRise) return true;
            if (hill.getxValues().gradientsPercent().get(i) > abruptRise || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-(i+1)) > abruptRise) return true;

        }

        range = abruptRiseRange > hill.getyValues().gradientsPercent().size() ? hill.getyValues().gradientsPercent().size() : abruptRiseRange;
        for (int i = 0; i < range ; i++) {
            if (hill.getyValues().gradientsPercent().get(i) < -abruptRise || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) < -abruptRise) return true;
            if (hill.getyValues().gradientsPercent().get(i) > abruptRise || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-(i+1)) > abruptRise) return true;
        }
        return false;
    }

    /**
     * Zählt die Anzahl an Objekten mit dem Attribut "Flatness" in einer vorgegebenen Liste aus Objekten.
     * @param - hills Liste mit Objekten
     * @return - Anzahl an Objekten mit dem Attribut "Flatness".
     */
    private double amountObjectFlat(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.isFlat(h)) counter++;
        }
        return counter;
    }

    /**
     * Zählt die Anzahl an Objekten mit dem Attribut "Symmetrie" in einer vorgegebenen Liste aus Objekten.
     * @param - hills Liste mit Objekten
     * @return - Anzahl an Objekten mit dem Attribut "Symmetrie".
     */
    private double amountObjectSym(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.isSymetric(h)) counter++;
        }
        return counter;
    }

    /**
     * Zählt die Anzahl an Objekten mit dem Attribut "abruptRise" in einer vorgegebenen Liste aus Objekten.
     * @param - hills Liste mit Objekten
     * @return - Anzahl an Objekten mit dem Attribut "abruptRise".
     */
    private double amountObjectSharp(List<XYHill> hills) {
        double counter = 0;
        for (XYHill h : hills) {
            if (this.hasAbruptRise(h)) counter++;
        }
        return counter;
    }

    /**
     * Erstellt Trainingsdaten aus vorgegebenen Listen und setzt den Boolean "isTrained" auf true, sobald durchlaufen.
     * @param aHills - Liste mit Objekten
     * @param bHills - Liste mit Objekten
     */
    public boolean training(List<XYHill> aHills, List<XYHill> bHills) {

        if (aHills.isEmpty()) {
            System.out.println("Keine Daten für die A-Objekte, trainieren nicht möglich");
            isTrained = false;
            return false;
        }

        if (bHills.isEmpty()) {
            System.out.println("Keine Daten für die B-Objekte, trainieren nicht möglich");
            isTrained = false;
            return false;
        }

        System.out.println("---------A-OBJEKTE: " + aHills.size() + "----------");
        System.out.println("--Sym " + amountObjectSym(aHills));
        System.out.println("--Flat " + amountObjectFlat(aHills));
        System.out.println("--Abrupt Rise " + amountObjectSharp(aHills));
        System.out.println("---------B-OBJEKTE: " + bHills.size() + "----------");
        System.out.println("--Sym " + amountObjectSym(bHills));
        System.out.println("--Flat " + amountObjectFlat(bHills));
        System.out.println("--Abrupt Rise " + amountObjectSharp(bHills));

        pAsym = amountObjectSym(aHills) /  aHills.size();
        pAflat =   amountObjectFlat(aHills) /  aHills.size();
        pAabruptRise = amountObjectSharp(aHills) /  aHills.size();

        pBsym = amountObjectSym(bHills) /  bHills.size();
        pBflat =  amountObjectFlat(bHills) /  bHills.size();
        pBabruptRise = amountObjectSharp(bHills) /  bHills.size();

        System.out.println("-----Wahrscheinlichkeiten A -----");
        System.out.println(("P(Sym|A) = " + pAsym));
        System.out.println(("P(Flat|A) = " + pAflat));
        System.out.println(("P(Abrupt Rise|A) = " + pAabruptRise));
        System.out.println("-----Wahrscheinlichkeiten B -----");
        System.out.println(("P(Sym|B) = " + pBsym));
        System.out.println(("P(Flat|B) = " + pBflat));
        System.out.println(("P(Abrupt Rise|B) = " + pBabruptRise));

        isTrained = true;
        return true;
    }

}
