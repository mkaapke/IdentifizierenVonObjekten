import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {

        java.io.BufferedReader data = new java.io.BufferedReader(new java.io.FileReader(new java.io.File("data.csv")));
        java.io.BufferedReader b0 = new java.io.BufferedReader(new java.io.FileReader(new java.io.File("B0.csv")));
        java.io.BufferedReader a0 = new java.io.BufferedReader(new java.io.FileReader(new java.io.File("A0.csv")));

        CSVReader readerB0 = new CSVReader(b0);
        CSVReader readerA0 = new CSVReader(a0);
        CSVReader readerData = new CSVReader(data);

        HashMap mapB0 = new HashMap();
        HashMap mapA0 = new HashMap();
        HashMap<Integer, List<String>> mapData = new HashMap();

        int i = 1;
        int j = 1;
        int k = 1;

        String[] listeB0 = {};
        String[] listeA0 = {};
        String[] dataList = {};

        /*
        Eine Map wird mit den Daten der Datei B0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeB0 = readerB0.readNext()) != null) {
            mapB0.put(i, listeB0[0]+ "," +listeB0[1]);

            i++;
        }

        /*
        Eine Map wird mit den Daten der Datei A0.csv gefüllt
        Key: Zeilennummer  Value: String mit dem Wert der Zeile
         */
        while ((listeA0 = readerA0.readNext()) != null) {
            mapA0.put(j, listeA0[0]+ "," +listeA0[1]);

            j++;
        }


        /*
        Eine Map wird mit den ersten 50 Zeilen der data.csv Datei gefüllt
        Key: Zeilennummer  Value: Liste mit den Werten der Zeile
         */
        for (int q = 0; q < 1000; q++) {
            List<String> line = new ArrayList<String>();
            dataList = readerData.readNext();

            for (String a : dataList) {
                line.add(a);
            }
            mapData.put(k, line);

            k++;
        }

        //System.out.println(mapB0.toString());
        System.out.println(find(mapData, mapA0));
        //System.out.println(mapB0.size());
        //System.out.println(mapA0.size());
    }

    public static int find (HashMap map, HashMap zahl) {
        int count = 0;
        for (int i = 1; i <= zahl.size(); i++) {
            if (map.get(i).toString().contains(zahl.get(i).toString())) {
                count++;
            }
        }
        return count;

    }
}