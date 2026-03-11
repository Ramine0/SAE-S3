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

    // initialise la matrice d'adjacence qui contient les voisins de chaque table
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

                    for (Table neighbour : tables)
                    {
                        if (neighbour != null)
                            if (neighbour.getNum() > t.getNum())
                            {
                                int neighbourX = neighbour.getCoord()[0];
                                int neighbourY = neighbour.getCoord()[1];

                                if (hasNeighbour(x, neighbourX, y, neighbourY))
                                {
                                    matriceAdj[t.getNum()][neighbour.getNum()] = 1;
                                    matriceAdj[neighbour.getNum()][t.getNum()] = 1;
                                }
                            }
                    }
                }
            }
        }

    }

    private boolean hasNeighbour(int x, int neighBourX, int y, int neighBourY)
    {
        return ((x - neighBourX) * (x - neighBourX) == 1) && y == neighBourY || ((y - neighBourY) * (y - neighBourY) == 1) && x == neighBourX)
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
