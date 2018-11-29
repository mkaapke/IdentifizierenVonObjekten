import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {

    final static int datasize = 4942;
    final static int testdata = 1000;

    public static void main(String[] args) throws IOException {

        BufferedReader data = new BufferedReader(new FileReader(new File("src/main/data.csv")));
        BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/A0.csv")));
        BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/B0.csv")));
        BufferedReader a1 = new BufferedReader(new FileReader(new File("src/main/A1.csv")));
        BufferedReader b1 = new BufferedReader(new FileReader(new File("src/main/B1.csv")));

        CSVReader readerData = new CSVReader(data);

        List<XYPoint> a0Points = getPoints(new CSVReader(a0));
        List<XYPoint> b0Points = getPoints(new CSVReader(b0));
        List<XYPoint> a1Points = getPoints(new CSVReader(a1));
        List<XYPoint> b1Points = getPoints(new CSVReader(b1));

        XYMatrix xyMatrix = new XYMatrix();
        XYHillClassifier classifier;

        /*
        Eine Map wird mit der von datasize vordefinierten Anzahl von Zeilen der data.csv Datei gefüllt
        Key: Zeilennummer Value: Liste mit den Werten der Zeile
         */
        for (int q = 0; q < datasize; q++) {
            List<String> line = new ArrayList<String>();
            String [] dataList = readerData.readNext();
            for (String a : dataList) {
                line.add(a);
                Integer toPut = Integer.valueOf(a.replace(".", ""));
                xyMatrix.put(q+1, toPut);
            }
        }
        readerData.close();

        classifier = new XYHillClassifier();
        classifier.training(xyMatrix.getHills(a0Points), xyMatrix.getHills(b0Points));
        System.out.println("-------------------------------------");
        Double falsePositives = (100.0 / a1Points.size()) * classifier.findBObjects(xyMatrix.getHills(a1Points)).size();
        Double truePositives = (100.0 / b1Points.size()) * classifier.findBObjects(xyMatrix.getHills(b1Points)).size();
        System.out.println("B-False Positives: "  + falsePositives + "%" );
        System.out.println("B-True Positives: "  + truePositives + "%" );
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static List<XYPoint> getPoints(CSVReader reader) throws IOException {
        int counter = 0;
        List<XYPoint> points = new ArrayList<>();
        String[] liste = {};
        while ((liste = reader.readNext()) != null) {
            if (Integer.valueOf(liste[1]) < datasize) {
                points.add(new XYPoint(Integer.valueOf(liste[1]), Integer.valueOf(liste[0])));
                if (++counter == testdata) break;
            }
        }
        reader.close();
        return points;
    }









}
