package placement;

import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileReader;
import java.util.Scanner;

public class GridMap extends Map {

    private int[] numbersOfTables;
    private int[][] matriceAdj;

    public GridMap(Table[] tables) {
        init(tables);
    }

    // initialise la matrice d'adjacence qui contient les voisins de chaque table
    private void init(Table[] tables) {

        matriceAdj = new int[tables.length + 1][tables.length + 1];
        if (tables[0].getCoord()[0] != -1) {

            int cpt = 0;
            for (Table t : tables) {
                if (t != null) {
                    int x = t.getCoord()[0];
                    int y = t.getCoord()[1];
                    int cptVois = 0;
                    for (Table vois : tables) {
                        if (vois != null) {
                            if (cptVois > cpt) {
                                int neighbourX = vois.getCoord()[0];
                                int neighbourY = vois.getCoord()[1];

                                if (hasNeighbour(x, neighbourX, y, neighbourY)) {
                                    matriceAdj[cpt][cptVois] = 1;
                                    matriceAdj[cptVois][cptVois] = 1;
                                }
                            }
                        }
                        cptVois++;
                    }
                }
                cpt++;
            }
        }

    }

    private boolean hasNeighbour(int x, int neighbourX, int y, int neighbourY) {
        return (((x - neighbourX) * (x - neighbourX) == 1) && y == neighbourY || ((y - neighbourY) * (y - neighbourY) == 1) && (x == neighbourX));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private Table[] chargerPlanDefaut(String path) {
        Table.reset();
        Table[] result;

        try {
            Scanner scan = new Scanner(new FileReader(path + "resources/planDefaut.csv"));
            result = new Table[scan.nextInt()];
            numbersOfTables = new int[result.length];

            String[] line;
            int cpt = 0;

            while (scan.hasNextLine()) {
                line = scan.nextLine().split(";");

                if (!line[0].isEmpty()) {
                    result[cpt] = new Table(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
                    numbersOfTables[cpt] = result[cpt].getNum();
                    cpt++;
                }
            }

            scan.close();

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("je me disais aussi");

            return null;
        }

    }

    @Override
    public int[] neighbours(int tableNumber, int[] available) {
        int[] neighbours = new int[9];
        int cpt = 0;
        int index = getIndexFromNumber(tableNumber);

        if (index > 0) {
            for (int i : matriceAdj[index]) {
                if (matriceAdj[index][i] == 1 && Utilitaire.in(numbersOfTables[i], available)) {
                    neighbours[cpt] = numbersOfTables[i];
                    cpt++;
                }
            }
        }

        return neighbours;
    }

    public Table[] loadMap(String path) {
        Table[] result = chargerPlanDefaut(path);

        if (result != null)
            init(result);

        return result;
    }

    private int getIndexFromNumber(int number) {
        for (int i = 0; i < numbersOfTables.length; i++) {
            if (number == numbersOfTables[i])
                return i;
        }

        return -1;
    }
}
