import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {

    final static int datasize = 4943;
    final static int testdata = 1000;

    final static String dataPath = "src/main/data2.csv";
    final static String trainingDataAPath = "src/main/A0.csv";
    final static String trainingDataBPath = "src/main/B0.csv";
    final static String testDataAPath = "src/main/A1.csv";
    final static String testDataBPath = "src/main/B1.csv";

    /**
     * Es werden die data.csv sowie die weiteren Listen mit den gesuchten Objekten eingelesen.
     * Mit Hilfe von Methoden werden die B-False Positives und B-True Positives ausgegeben.
     */
    public static void main(String[] args) throws IOException {

        BufferedReader data = new BufferedReader(new FileReader(new File(dataPath)));
        BufferedReader a0 = new BufferedReader(new FileReader(new File(trainingDataAPath)));
        BufferedReader b0 = new BufferedReader(new FileReader(new File(trainingDataBPath)));
        BufferedReader a1 = new BufferedReader(new FileReader(new File(testDataAPath)));
        BufferedReader b1 = new BufferedReader(new FileReader(new File(testDataBPath)));

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
        Double falsePositives = (100.0 / a1Points.size()) * classifier.findABObjects(xyMatrix.getHills(a1Points), false).size();
        Double truePositives = (100.0 / b1Points.size()) * classifier.findABObjects(xyMatrix.getHills(b1Points), false).size();
        System.out.println("B-False Positives: "  + falsePositives + "%" );
        System.out.println("B-True Positives: "  + truePositives + "%" );
    }

    /**
     * Rundet eine Zahl auf.
     * @param value Zu rundende Zahl.
     * @param places Setzt die Anzahl an Zahlen nach dem Komma fest.
     * @return gerundeter Double-Value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Erstellt eine Liste mit x und y Punkten aus einer gegebenen CSV-Datei.
     * @param reader ein CSVReader angwendet auf eine CSV-Datei
     * @return Eine Liste mit x und y Punkten
     * @throws IOException
     */
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
