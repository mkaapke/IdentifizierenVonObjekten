import java.util.List;

public class XYHillClassifier {

    public boolean isBObject(XYHill hill) {

        return true;
    }


    public boolean isAObject(XYHill hill) {

        return false;
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
