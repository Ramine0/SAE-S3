package placement;

import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileReader;
import java.util.Scanner;

public class GridMap extends Map
{

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

            for (Table t : tables)
            {
                if (t != null)
                {
                    int x = t.getCoord()[0];
                    int y = t.getCoord()[1];
                    for (Table vois : tables)
                    {
                        if (vois != null)
                        {
                            if (vois.getNum() > t.getNum())
                            {

                                int xVois = vois.getCoord()[0];
                                int yVois = vois.getCoord()[1];

                                // l'idée ici c'est de dire que si les deux ont une distance de 1 alors ils sont voisins
                                // plutot que de faire une abs() pour les -1 j'ai fait un carré car évite d'importer la librairie et tt
                                if (((x - xVois) * (x - xVois) == 1) && y == yVois || ((y - yVois) * (y - yVois) == 1) && x == xVois)
                                {
                                    matriceAdj[t.getNum()][vois.getNum()] = 1;
                                    matriceAdj[vois.getNum()][t.getNum()] = 1;
                                }
                            }
                        }

                    }
                }
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
            System.out.println(path+"resources/planDefaut.csv");
            Scanner scan = new Scanner(new FileReader(path + "resources/planDefaut.csv"));
            lesTables = new Table[scan.nextInt()];
            String[] line;
            int cpt = 0;
            while (scan.hasNextLine())
            {
                line = scan.nextLine().split(";");
                if (!line[0].isEmpty())
                {
                    lesTables[cpt] = new Table(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                    cpt++;
                }
            }

            scan.close();
            return lesTables;
        } catch (Exception e)
        {
            System.out.println(path+"resources/planDefaut.csv");
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public int[] neighbours(int numTable, int[] dispo)
    {

        int[] voisins = new int[9];
        int cpt = 0;
        for (int i : matriceAdj[numTable])
        {
            if (matriceAdj[numTable][i] == 1 && Utilitaire.in(i, dispo))
            {
                voisins[cpt] = i;
                cpt++;
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

}
