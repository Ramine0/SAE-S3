package placement;

import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GridMap extends Map {

    private int[][] matriceAdj;
    private int[] numbersOfTables;

    public GridMap() {
    }

    public GridMap(Table[] tables) {
        init(tables);
    }

    // initialise la matrice d'adjacence qui contient les voisins de chaque table
    private void init(Table[] tables) {
        matriceAdj = new int[tables.length + 1][tables.length + 1];

        if (tables[0].getCoordinates()[0] != -1)
            for (int i = 0; i < tables.length; i++)
                if (tables[i] != null) {
                    int x = tables[i].getCoordinates()[0];
                    int y = tables[i].getCoordinates()[1];

                    for (int j = 0; j < tables.length; j++)
                        if (tables[j] != null)
                            if (j > i) {
                                int neighbourX = tables[j].getCoordinates()[0];
                                int neighbourY = tables[j].getCoordinates()[1];

                                if (hasNeighbour(x, neighbourX, y, neighbourY)) {
                                    matriceAdj[i][j] = 1;
                                    matriceAdj[j][j] = 1;
                                }
                            }
                }

    }

    private boolean hasNeighbour(int x, int neighbourX, int y, int neighbourY) {
        return (((x - neighbourX) * (x - neighbourX) == 1) && y == neighbourY || ((y - neighbourY) * (y - neighbourY) == 1) && (x == neighbourX));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private Table[] chargerPlanDefaut(String path) {
        Table.reset();

        try {
            Scanner scan = new Scanner(new FileReader(path + "resources/planDefaut.csv"));
            List<Table> result = new ArrayList<>();

            numbersOfTables = new int[scan.nextInt()];

            String[] line;

            while (scan.hasNextLine()) {
                line = scan.nextLine().split(";");

                if (!line[0].isEmpty()) {
                    Table table = new Table(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));

                    result.add(table);
                    numbersOfTables[result.size() - 1] = table.getNumber();
                }
            }

            scan.close();

            return result.toArray(new Table[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("je me disais aussi");

            return null;
        }
    }

    @Override
    public int[] neighbours(int tableNumber, int[] available) {
        List<Integer> result = new ArrayList<>();
        int index = getIndexFromNumber(tableNumber);

        if (index > 0)
            for (int i : matriceAdj[index])
                if (matriceAdj[index][i] == 1 && Utilitaire.in(numbersOfTables[i], available))
                    result.add(numbersOfTables[i]);

        return result.stream().mapToInt(i->i).toArray();
    }

    public Table[] loadMap(String path) {
        Table[] result = chargerPlanDefaut(path);

        if (result != null)
            init(result);

        return result;
    }

    private int getIndexFromNumber(int number) {
        for (int i = 0; i < numbersOfTables.length; i++)
            if (number == numbersOfTables[i])
                return i;

        return -1;
    }
}
