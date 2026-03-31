package placement;

import jakarta.transaction.Transactional;
import org.the_disabled.sae_s3.Table;
import utilitaire.Utilitaire;

import java.io.FileReader;
import java.util.Scanner;

public class GridMap extends Map {

    private int[] tableNumber;
    private int[][] adjacencyMatrix;

    public GridMap() {

    }

    public GridMap(Table[] tables) {

        init(tables);
    }


    private void init(Table[] tables) {

        adjacencyMatrix = new int[tables.length + 1][tables.length + 1];
        if (tables[0].getCoordinates()[0] != -1) {

            int counter = 0;
            for (Table table : tables) {
                if (table != null) {
                    int x = table.getCoordinates()[0];
                    int y = table.getCoordinates()[1];
                    int counterNeighbour = 0;
                    for (Table neighbour : tables) {
                        if (neighbour != null)
                            if (counterNeighbour > counter) {
                                int neighbourX = neighbour.getCoordinates()[0];
                                int neighbourY = neighbour.getCoordinates()[1];

                                if (hasNeighbour(x, neighbourX, y, neighbourY)) {
                                    adjacencyMatrix[counter][counterNeighbour] = 1;
                                    adjacencyMatrix[counterNeighbour][counterNeighbour] = 1;
                                }
                            }
                        counterNeighbour++;
                    }
                } else {
                    System.out.println("TABLE NULL");
                }
                counter++;
            }
        }
    }

    @Override
    public int[] neighbours(int table, int[] existing) {
        int[] neighbours = new int[9];
        int cpt = 0;
        int index = getIndexFromNumber(table);

        if (index > 0)
            for (int i = 0; i < adjacencyMatrix[index].length; i++) {
                if (adjacencyMatrix[index][i] == 1 && Utilitaire.in(tableNumber[i], existing)) {
                    neighbours[cpt] = tableNumber[i];
                    cpt++;
                }
            }

        return neighbours;
    }

    private boolean hasNeighbour(int x, int neighbourX, int y, int neighbourY) {
        boolean hasNeighbourX = (x - neighbourX) * (x - neighbourX) == 1;
        boolean hasNeighbourY = (y - neighbourY) * (y - neighbourY) == 1;

        return (hasNeighbourX && y == neighbourY || hasNeighbourY && (x == neighbourX) || hasNeighbourX && hasNeighbourY);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private Table[] loadDefaultMap(String path) {
        Table.reset();
        Table[] result;

        try {
            Scanner scan = new Scanner(new FileReader(path + "resources/planDefaut.csv"));
            result = new Table[scan.nextInt()];
            tableNumber = new int[result.length];
            String[] line;
            int cpt = 0;

            while (scan.hasNextLine()) {
                line = scan.nextLine().split(";");

                if (!line[0].isEmpty()) {
                    result[cpt] = new Table(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
                    tableNumber[cpt] = result[cpt].getNumber();
                    cpt++;
                }
            }

            scan.close();

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }

    }

    public Table[] loadMap(String path) {
        Table[] result = loadDefaultMap(path);

        if (result != null) {
            init(result);
        }
        return result;
    }

    private int getIndexFromNumber(int number) {
        for (int i = 0; i < tableNumber.length; i++)
            if (number == tableNumber[i])
                return i;

        return -1;
    }
}