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

        List<XYPoint> a0Points = new ArrayList<XYPoint>();
        List<XYPoint> b0Points = new ArrayList<XYPoint>();

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

        System.out.println("---------ABOJEKTE: " + a0Points.size() + "----------");
        List<XYHill> hills = xyMatrix.getHills(a0Points);

        double aListe = a0Points.size();

        double flatA = classifier.anzObjektFlat(hills);
        double symA = classifier.anzObjektSym(hills);
        double sharpA = classifier.anzObjektSharp(hills);

        System.out.println("--Flat " + classifier.anzObjektFlat(hills));
        System.out.println("--Sym " + classifier.anzObjektSym(hills));
        System.out.println("--Sharp " + classifier.anzObjektSharp(hills));

        System.out.println("---------BOBJEKTE: " + b0Points.size() + "----------");
        hills = xyMatrix.getHills(b0Points);

        double bListe = b0Points.size();

        double flatB = classifier.anzObjektFlat(hills);
        double symB = classifier.anzObjektSym(hills);
        double sharpB = classifier.anzObjektSharp(hills);

        System.out.println("--Flat " + classifier.anzObjektFlat(hills));
        System.out.println("--Sym " + classifier.anzObjektSym(hills));
        System.out.println("--Sharp " + classifier.anzObjektSharp(hills));

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

        System.out.println(classifier.findAObjects( xyMatrix.getHills(a0Points)));
        System.out.println("-------------------------------------");
        System.out.println(classifier.findAObjects( xyMatrix.getHills(b0Points)));


    }









}
