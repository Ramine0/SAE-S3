package placement;

import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GridMap extends Map
{

    private int[] numbersOfTables ;
    private int[][] matriceAdj;

    public GridMap(Table[] tables)
    {
        init(tables);
    }

    private void init(Table[] tables)
    {

        matriceAdj = new int[tables.length + 1][tables.length + 1];
        if (tables[0].getCoord()[0] != -1)
        {

            int cpt = 0 ;
            for (Table t : tables)
            {
                if (t != null)
                {
                    int x = t.getCoord()[0];
                    int y = t.getCoord()[1];
                    int cptVois = 0;
                    for (Table vois : tables)
                    {
                        if (vois != null)
                        {
                            if (cptVois > cpt)
                            {

                                int xVois = vois.getCoord()[0];
                                int yVois = vois.getCoord()[1];

                                // l'idée ici c'est de dire que si les deux ont une distance de 1 alors ils sont voisins
                                // plutot que de faire une abs() pour les -1 j'ai fait un carré car évite d'importer la librairie et tt
                                if (((x - xVois) * (x - xVois) == 1) && y == yVois || ((y - yVois) * (y - yVois) == 1) && x == xVois)
                                {
                                    matriceAdj[cpt][cptVois] = 1;
                                    matriceAdj[cptVois][cptVois] = 1;
                                }
                            }
                        }
                        cptVois ++ ;
                    }
                }
                cpt ++ ;
            }
        }


    }

    public GridMap()
    {
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private Table[] chargerPlanDefaut(String path)
    {
        Table.reset();
        Table[] lesTables;
        try
        {
            Scanner scan = new Scanner(new FileReader(path + "resources/planDefaut.csv"));
            lesTables = new Table[scan.nextInt()];
            String[] line;
            int cpt = 0;
            while (scan.hasNextLine())
            {
                line = scan.nextLine().split(";");
                if (!line[0].isEmpty())
                {
                    lesTables[cpt] = new Table(Integer.parseInt(line[0]), Integer.parseInt(line[1]),Integer.parseInt(line[2]));
                    cpt++;
                }
            }

            scan.close();

            return lesTables;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());

            System.out.println("je me disai aussi");

            return null;
        }

    }

    @Override
    public int[] neighbours(int numTable, int[] dispo)
    {

        int[] voisins = new int[9];
        int cpt = 0;
        int index = getIndexFromNum(numTable);
        if (index > 0) {
            for (int i : matriceAdj[index]) {
                if (matriceAdj[index][i] == 1 && Utilitaire.in(numbersOfTables[i], dispo)) {
                    voisins[cpt] = numbersOfTables[i];
                    cpt++;
                }
            }
        }
        return voisins;
    }

    public Table[] loadMap(String path)
    {
        Table[] tables = chargerPlanDefaut(path);
        if (tables != null)
        {
            init(tables);
        }
        return tables;
    }

    private int getIndexFromNum(int num) {
        for (int i = 0 ; i < numbersOfTables.length ; i++) {
            if (num == numbersOfTables[i]) {
                return i ;
            }
        }
        return -1 ;
    }


}
