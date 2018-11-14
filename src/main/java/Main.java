import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        final int datasize = 3000;

        BufferedReader data = new BufferedReader(new FileReader(new File("src/main/data.csv")));
        BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/B0.csv")));
        BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/A0.csv")));

        CSVReader readerB0 = new CSVReader(b0);
        CSVReader readerA0 = new CSVReader(a0);
        CSVReader readerData = new CSVReader(data);

        HashMap<Integer, Integer> mapB0 = new HashMap();
        HashMap<Integer, Integer> mapA0 = new HashMap();

        XYMatrix xyMatrix = new XYMatrix();

        List<Graph> graphs;

        String[] listeB0 = {};
        String[] listeA0 = {};
        String[] dataList = {};

         /*
        Eine Map wird mit den Daten der Datei B0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeB0 = readerB0.readNext()) != null) {
            mapB0.put(Integer.valueOf(listeB0[0]), Integer.valueOf(listeB0[1]));
        }

        /*
        Eine Map wird mit den Daten der Datei A0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeA0 = readerA0.readNext()) != null) {
            mapA0.put(Integer.valueOf(listeA0[0]), Integer.valueOf(listeA0[1]));
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
                xyMatrix.put(q, toPut);
            }
        }

        readerA0.close();
        readerB0.close();
        readerData.close();

        graphs = findGraphs(xyMatrix);

        for (Graph g : graphs) {

            for (Map.Entry<Integer, Integer> entry : mapA0.entrySet()) {
                if (g.getxRow() == entry.getValue()) {
                    if (g.getValues().contains(xyMatrix.get(entry.getValue(), entry.getValue()))) {
                        System.out.println("-------A------");
                        System.out.println(g);
                        System.out.println("Steigung LINKS: " + g.riseProcent());
                        System.out.println("Steigung RECHTS: " + g.fallProcent());
                        System.out.println("");
                    }
                }
            }
            for (Map.Entry<Integer, Integer> entry : mapB0.entrySet()) {
                if (g.getxRow() == entry.getValue()) {
                    if (g.getValues().contains(xyMatrix.get(entry.getValue(), entry.getKey()))) {
                        System.out.println("-------B------");
                        System.out.println(g);
                        System.out.println("Steigung LINKS: " + g.riseProcent());
                        System.out.println("Steigung RECHTS: " + g.fallProcent());
                        System.out.println("");
                    }
                }
            }
        }

    }

    public static List<Graph> findGraphs(XYMatrix xyMatrix) {
        List<Graph> graphs = new ArrayList<Graph>();
        Integer graphNumber = -1;

        for (Map.Entry<Integer, List<Integer>> entry : xyMatrix.getValues().entrySet()) {
            Integer acutalValue = entry.getValue().get(0);
            Integer steigung = -1;
            for (Integer i : entry.getValue()) {
                if (i > acutalValue && steigung != 0) {
                    steigung = 2;
                }
                if (i < acutalValue && steigung != 1) {
                    steigung = 1;
                }

                if (steigung == 0) {
                    graphs.get(graphNumber).addValue(i);
                }

                if (steigung == 1) {
                    graphs.get(graphNumber).addValue(i);
                }

                if (steigung == 2) {
                    steigung = 0;
                    graphNumber++;
                    graphs.add(new Graph(entry.getKey()));
                    graphs.get(graphNumber).addValue(i);
                }

                if (steigung == -1) {
                    steigung = entry.getValue().get(0) < entry.getValue().get(1) ? 0 : 1 ;
                    graphNumber++;
                    graphs.add(new Graph(entry.getKey()));
                    graphs.get(graphNumber).addValue(i);
                }
                acutalValue = i;

            }

        }
        return graphs;
    }

}
