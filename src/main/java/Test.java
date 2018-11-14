import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {

//
        BufferedReader data = new BufferedReader(new FileReader(new File("src/main/data.csv")));
        BufferedReader b0 = new BufferedReader(new FileReader(new File("src/main/B0.csv")));
        BufferedReader a0 = new BufferedReader(new FileReader(new File("src/main/A0.csv")));

        CSVReader readerB0 = new CSVReader(b0);
        CSVReader readerA0 = new CSVReader(a0);
        CSVReader readerData = new CSVReader(data);

        HashMap<Integer, Integer> mapB0 = new HashMap();
        HashMap<Integer, Integer> mapA0 = new HashMap();
        HashMap<Integer, List<String>> mapData = new HashMap();

        XYMatrix xyMatrix = new XYMatrix();

        int k = 1;

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
        for (int q = 0; q < 3000; q++) {
            List<String> line = new ArrayList<String>();
            dataList = readerData.readNext();

            for (String a : dataList) {
                line.add(a);
                xyMatrix.put(q, Integer.valueOf(a.replace(".","")));
            }
            mapData.put(k, line);
            k++;
        }
        int ok = 0;
        for (Map.Entry entryA : mapA0.entrySet()) {
            if (Integer.valueOf(entryA.getValue().toString()) < 3000) {
                if (Integer.valueOf(entryA.getKey().toString()) < 3000) {
                    System.out.println(entryA.getKey()+","+entryA.getValue());
                    ok++;
                }
            }
        }
        System.out.println(ok);
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

    public static List<Integer> findObjects(Map<Integer, Integer> Coordinaten, XYMatrix data) {
        List<Integer> aObjects = new ArrayList<Integer>();
        for (Map.Entry entry : Coordinaten.entrySet()) {
            if (data.containsXY((Integer) entry.getKey(), (Integer) entry.getValue())) {
                aObjects.add(data.get((Integer) entry.getKey(), (Integer) entry.getValue()));
            }
        }
        Collections.sort(aObjects);
        return aObjects;
    }

}