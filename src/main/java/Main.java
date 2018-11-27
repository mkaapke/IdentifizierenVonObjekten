import com.opencsv.CSVReader;
import sun.awt.Symbol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    final static int datasize = 4942;
    final static int testdata = 100;

    public static void main(String[] args) throws IOException {

        BufferedReader data = new BufferedReader(new FileReader(new File("src/main/data.csv")));
        BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/A1.csv")));
        BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/B1.csv")));

        CSVReader readerB0 = new CSVReader(b0);
        CSVReader readerA0 = new CSVReader(a0);
        CSVReader readerData = new CSVReader(data);

        List<XYPoint> a0Points = new ArrayList<>();
        List<XYPoint> b0Points = new ArrayList<>();

        XYMatrix xyMatrix = new XYMatrix();
        XYHillClassifier classifier;

        String[] listeB0 = {};
        String[] listeA0 = {};
        String[] dataList = {};

        int counter = 0;

         /*
        Eine Map wird mit den Daten der Datei B0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeB0 = readerB0.readNext()) != null) {
            if (Integer.valueOf(listeB0[1]) < datasize) b0Points.add(new XYPoint(Integer.valueOf(listeB0[1]), Integer.valueOf(listeB0[0])));
            if (++counter == testdata) break;
        }

        /*
        Eine Map wird mit den Daten der Datei A0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        counter = 0;
        while ((listeA0 = readerA0.readNext()) != null) {
            if (Integer.valueOf(listeA0[1]) < datasize) a0Points.add(new XYPoint(Integer.valueOf(listeA0[1]), Integer.valueOf(listeA0[0])));
            if (++counter == testdata) break;
        }

        /*
        Eine Map wird mit den erstene 50 Zeilen der data.csv Datei gefüllt
        Key: Zeilennummer Value: Liste mit den Werten der Zeile
         */
        for (int q = 0; q < datasize; q++) {
            List<String> line = new ArrayList<String>();
            dataList = readerData.readNext();

            for (String a : dataList) {
                line.add(a);
                Integer toPut = Integer.valueOf(a.replace(".", ""));
                xyMatrix.put(q+1, toPut);
            }
        }

        readerA0.close();
        readerB0.close();
        readerData.close();

        classifier = new XYHillClassifier(a0Points.size(), b0Points.size());

        classifier.training(xyMatrix.getHills(a0Points), xyMatrix.getHills(b0Points));

        classifier.findBObjects( xyMatrix.getHills(a0Points));
        System.out.println("-------------------------------------");
        classifier.findBObjects( xyMatrix.getHills(b0Points));


    }









}
