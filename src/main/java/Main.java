import com.opencsv.CSVReader;
import sun.awt.Symbol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        final int datasize = 5;

        //BufferedReader data = new BufferedReader(new FileReader(new File("src/main/data.csv")));
        BufferedReader data = new BufferedReader(new FileReader(new File("src/main/testdata.txt")));
        BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/B0.csv")));
        BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/A0.csv")));

        CSVReader readerB0 = new CSVReader(b0);
        CSVReader readerA0 = new CSVReader(a0);
        CSVReader readerData = new CSVReader(data);

        HashMap<Integer, Integer> mapB0 = new HashMap();
        HashMap<Integer, Integer> mapA0 = new HashMap();

        XYMatrix xyMatrix = new XYMatrix();

        List<Graph> graphsX;
        List<Graph> graphsY;

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

        System.out.println(xyMatrix);
        System.out.println(xyMatrix.findXYMaxinRange(3,3,1)[0] + "/" + xyMatrix.findXYMaxinRange(3,3,1)[1]);

        graphsX = findXGraphs(xyMatrix);
        graphsY = findXGraphs(xyMatrix.rotate());

        //System.out.println(graphsX.get(25).gradients());

        XYHill aHill = getHills(graphsX, graphsY, xyMatrix, mapA0).get(2);
        XYHill bHill = getHills(graphsX, graphsY, xyMatrix, mapB0).get(2);

        System.out.println(aHill.getY() + "/" + aHill.getX());
        System.out.println(aHill.getxRow().gradients());
        System.out.println(aHill.getyRow().gradients());
        System.out.println(bHill.getY() + "/" + bHill.getX());
        System.out.println(bHill.getxRow().gradients());
        System.out.println(bHill.getyRow().gradients());



        /*for (Graph g : graphs) {
            if (g.getRow() == 145) System.out.println(g);
            for (Map.Entry<Integer, Integer> entry : mapA0.entrySet()) {
                if (g.getRow() == entry.getValue()) {
                    if (g.getValuesZ().contains(xyMatrix.get(entry.getValue(), entry.getKey()))) {
                        System.out.println("-------A------");
                        System.out.println(g);
                        System.out.println("Steigung LINKS: " + g.riseProcent());
                        System.out.println("Steigung RECHTS: " + g.fallProcent());
                        System.out.println("");
                    }
                }
            }
            for (Map.Entry<Integer, Integer> entry : mapB0.entrySet()) {
                if (g.getRow() == entry.getValue()) {
                    if (g.getValuesZ().contains(xyMatrix.get(entry.getValue(), entry.getKey()))) {
                        System.out.println("-------B------");
                        System.out.println(g);
                        System.out.println("Steigung LINKS: " + g.riseProcent());
                        System.out.println("Steigung RECHTS: " + g.fallProcent());
                        System.out.println("");
                    }
                }
            }
        }*/

    }

    private static List<Graph> findXGraphs(XYMatrix xyMatrix) {
        List<Graph> graphs = new ArrayList<Graph>();
        int graphNumber = -1;


        for (Map.Entry<Integer, List<Integer>> entry : xyMatrix.entrySet()) {
            Integer currentValue = entry.getValue().get(0);
            int gradiantState = -1;
            Integer yTrack = 1;
            for (Integer nextValue : entry.getValue()) {
                if (nextValue > currentValue && gradiantState != 0) {
                    gradiantState = 2;
                }
                if (nextValue < currentValue && gradiantState != 1) {
                    gradiantState = 1;
                }

                if (gradiantState == 0) {
                    graphs.get(graphNumber).addValueZ(nextValue);
                    graphs.get(graphNumber).addValueY(yTrack++);
                }

                if (gradiantState == 1) {
                    graphs.get(graphNumber).addValueZ(nextValue);
                    graphs.get(graphNumber).addValueY(yTrack++);
                }

                if (gradiantState == 2) {
                    gradiantState = 0;
                    graphNumber++;
                    graphs.add(new Graph(entry.getKey()+1));
                    graphs.get(graphNumber).addValueZ(nextValue);
                    graphs.get(graphNumber).addValueY(yTrack++);
                }

                if (gradiantState == -1) {
                    gradiantState = entry.getValue().get(0) < entry.getValue().get(1) ? 0 : 1 ;
                    graphNumber++;
                    graphs.add(new Graph(entry.getKey()+1));
                    graphs.get(graphNumber).addValueZ(nextValue);
                    graphs.get(graphNumber).addValueY(yTrack++);
                }
                currentValue = nextValue;

            }

        }
        return graphs;
    }

    private static Graph findGraph(List<Graph> graphs, XYMatrix xyMatrix, Integer x, Integer y) {
        for (Graph g : graphs) {
            if (g.containsValueY(y) && g.getRow().equals(x)) return g;
         }
        return null;
    }

    private static List<XYHill> getHills(List<Graph> xGraphs, List<Graph> yGraphs, XYMatrix xyMatrix, Map<Integer, Integer> points) {
        List<XYHill> hills = new ArrayList<XYHill>();

        for (Map.Entry<Integer, Integer> entry : points.entrySet()) {
            XYHill hill = new XYHill(findGraph(xGraphs, xyMatrix, entry.getValue(), entry.getKey()), findGraph(yGraphs, xyMatrix.rotate(), entry.getKey(), entry.getValue()), entry.getValue(), entry.getKey());
            hills.add(hill);
        }
        return hills;
    }


}
