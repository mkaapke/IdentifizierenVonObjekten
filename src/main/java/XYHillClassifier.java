import java.util.Collections;
import java.util.List;

public class XYHillClassifier {


    public static final double  attributeFlatnessDef = 0.2;
    public static final int triggerFlatnessPercent = 50;

    public static final double[] attributenotSymetricDef = {1.5, 0.5};
    public static final int triggerSymetricPercent = 10;

    public static final int attributeToSharpDef = 2;


    private XYMatrix xyMatrix;

    public boolean isBObject(XYHill hill) {
        return !isAObject(hill);
    }

    public XYHillClassifier(XYMatrix xyMatrix) {
        this.xyMatrix = xyMatrix;
    }

    public boolean isAObject(XYHill hill) {
        return !sharp(hill);
    }

    public boolean isSymetric(XYHill hill) {
        List<Integer> left = hill.getxValues().upGraph().gradientsInt();
        Collections.reverse(left);
        List<Integer> right = hill.getxValues().downGraph().gradientsInt();
        Integer rangeX = left.size() > right.size() ? right.size() : left.size();
        double wertX = 0;
        for (int i = 0 ;  i < rangeX ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();
            if (prozent < attributenotSymetricDef[0] && prozent > attributenotSymetricDef[1]) wertX++;
        }
        left = hill.getyValues().upGraph().gradientsInt();
        Collections.reverse(left);
        right = hill.getyValues().downGraph().gradientsInt();
        Integer rangeY = left.size() > right.size() ? right.size() : left.size();
        double wertY = 0;
        for (int i = 0 ;  i < rangeY ; i++) {
            double prozent = (left.get(i).doubleValue()*-1) / right.get(i).doubleValue();
            if (prozent < attributenotSymetricDef[0] && prozent > attributenotSymetricDef[1]) wertY++;
        }


        //System.out.println(hill);
        double symetricProportionX = wertX > 0 ? (100 / rangeX) * wertX : 0;
        double symetricProportionY = wertY > 0 ? (100 / rangeY) * wertY : 0;

        //if(symetricProportionX > triggerSymetricPercent && symetricProportionY > triggerSymetricPercent) System.out.println(hill);

        return symetricProportionX > triggerSymetricPercent && symetricProportionY > triggerSymetricPercent;
        //return (wertY != 0 && wertX != 0)  && ((100 / (rangeX + rangeY)) * wert) > triggerSymetricPercent;
    }

    public boolean flat(XYHill hill) {
        Integer flatness = 0;
        for (Double d : hill.getxValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        for (Double d : hill.getyValues().gradientsPercent()) if (d < attributeFlatnessDef &&  d > -attributeFlatnessDef) flatness++;
        return ((100 / (hill.getxValues().gradientsPercent().size() + hill.getyValues().gradientsPercent().size())) * flatness) > triggerFlatnessPercent ;
    }

    public boolean sharp(XYHill hill) {

        if (hill.getxValues().gradientsPercent().get(0) < -attributeToSharpDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-1) < -attributeToSharpDef) return true;
        if (hill.getxValues().gradientsPercent().get(1) < -attributeToSharpDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-2) < -attributeToSharpDef) return true;
        if (hill.getxValues().gradientsPercent().get(2) < -attributeToSharpDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-3) < -attributeToSharpDef) return true;
        if (hill.getyValues().gradientsPercent().get(0) < -attributeToSharpDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-1) < -attributeToSharpDef) return true;
        if (hill.getyValues().gradientsPercent().get(1) < -attributeToSharpDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-2) < -attributeToSharpDef) return true;
        if (hill.getyValues().gradientsPercent().get(2) < -attributeToSharpDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-3) < -attributeToSharpDef) return true;

        if (hill.getxValues().gradientsPercent().get(0) > attributeToSharpDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-1) > attributeToSharpDef) return true;
        if (hill.getxValues().gradientsPercent().get(1) > attributeToSharpDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-2) > attributeToSharpDef) return true;
        if (hill.getxValues().gradientsPercent().get(2) > attributeToSharpDef || hill.getxValues().gradientsPercent().get(hill.getxValues().gradientsPercent().size()-3) > attributeToSharpDef) return true;
        if (hill.getyValues().gradientsPercent().get(0) > attributeToSharpDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-1) > attributeToSharpDef) return true;
        if (hill.getyValues().gradientsPercent().get(1) > attributeToSharpDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-2) > attributeToSharpDef) return true;
        if (hill.getyValues().gradientsPercent().get(2) > attributeToSharpDef || hill.getyValues().gradientsPercent().get(hill.getyValues().gradientsPercent().size()-3) > attributeToSharpDef) return true;

        return false;
    }


}
