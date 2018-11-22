import com.opencsv.CSVReader;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        final int datasize = 4942;

        BufferedReader data = new BufferedReader(new FileReader(new File("src/main/data.csv")));
        //BufferedReader data = new BufferedReader(new FileReader(new File("src/main/testdata.txt")));
        //BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/testdataA")));
        BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/A0.csv")));
        BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/B0.csv")));
        //BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/testdataB")));

        CSVReader readerB0 = new CSVReader(b0);
        CSVReader readerA0 = new CSVReader(a0);
        CSVReader readerData = new CSVReader(data);

        HashMap<Integer, Integer> mapB0 = new HashMap();
        List<XYPoint> a0Points = new ArrayList<XYPoint>();
        List<XYPoint> b0Points = new ArrayList<XYPoint>();

        XYMatrix xyMatrix = new XYMatrix();
        XYHillClassifier classifier = new XYHillClassifier();

        List<XYGraph> graphsX;
        List<XYGraph> graphsY;

        String[] listeB0 = {};
        String[] listeA0 = {};
        String[] dataList = {};

         /*
        Eine Map wird mit den Daten der Datei B0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeB0 = readerB0.readNext()) != null) {
            b0Points.add(new XYPoint(Integer.valueOf(listeB0[1]), Integer.valueOf(listeB0[0])));
        }

        /*
        Eine Map wird mit den Daten der Datei A0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeA0 = readerA0.readNext()) != null) {
            a0Points.add(new XYPoint(Integer.valueOf(listeA0[1]), Integer.valueOf(listeA0[0])));
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

        List<XYHill> hills = xyMatrix.getHills(a0Points);
        int counter = 0;
        for (XYHill h : hills) counter+= classifier.isAObject(h) == 0 ? 1 : 0;

        System.out.println("----" + counter);

        counter = 0;
        System.out.println("--------------------");
        hills = xyMatrix.getHills(b0Points);
        for (XYHill h : hills) counter+= classifier.isAObject(h) != 0 ? 1 : 0;

        System.out.println("----" + counter);



    }



    //UNUSED
    private static XYGraph findGraph(List<XYGraph> XYGraphs, XYMatrix xyMatrix, Integer x, Integer y) {
        Integer[] maximumPoints = xyMatrix.findXYMaxinRange(y, x, 2);
        x = maximumPoints[0];
        for (XYGraph g : XYGraphs) {
            if (g.containsValueY(y) && g.getRow().equals(x)) return g;
         }
        return null;
    }

    //UNUSED
    private static List<XYHill> getHills(List<XYGraph> xXYGraphs, List<XYGraph> yXYGraphs, XYMatrix xyMatrix, Map<Integer, Integer> points) {
        List<XYHill> hills = new ArrayList<XYHill>();

        for (Map.Entry<Integer, Integer> entry : points.entrySet()) {
            //XYHill hill = new XYHill(findGraph(xXYGraphs, xyMatrix, entry.getValue(), entry.getKey()), findGraph(yXYGraphs, xyMatrix.rotate(), entry.getKey(), entry.getValue()), entry.getKey(), entry.getValue());
            Integer[] maximumPoints = xyMatrix.findXYMaxinRange(entry.getKey(), entry.getValue(), 2);
            Integer x = maximumPoints[0];
            Integer y = maximumPoints[1];
            XYHill hill = xyMatrix.findHill(x, y);
            hills.add(hill);
        }
        return hills;
    }


}
