package placement;

import utilitaire.Utilitaire;
import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends Map
{
    private final int width;
    private final int height;

    private int[] dispo;

    // a revoir car on ne prends pas en compte les tables supprimées et les L et l
    // je v m'en charger
    // oo ee aa
    @Override
    public int[] neighbours(int table, int[] dispo)
    {
        this.dispo = dispo;
        // Get the position of the table number (1-9) in the 1D array (0-8)
        int pos = Utilitaire.pos(table, dispo);

        // Define neighbor offsets
        int[][] offsets = {
                {0, -1},    // Above middle
                {1, -1},    // Top right
                {1, 0},     // Right
                {1, 1},     // Bottom right
                {0, 1},     // Below middle
                {-1, 1},    // Bottom left
                {-1, 0},    // Left
                {-1, -1}    // Top left
        };

        // Use a List to collect valid neighbor numbers
        List<Integer> validNeighbors = new ArrayList<>();

        for (int[] offset : offsets) {
            int neighborIndex = getNeighbour(pos, offset[0], offset[1],dispo.length);
            if (neighborIndex != -1) {
                validNeighbors.add(dispo[neighborIndex]); // Store the neighbor value
            }
        }


        // Convert List to an array and return
        return validNeighbors.stream().mapToInt(Integer::intValue).toArray();
    }

    private int getNeighbour(int index, int xOffset, int yOffset,int maxIndex) {
        int newIndex = index + xOffset + yOffset * width; // Calculate new index
        // pas de retour ligne
        if (index %width == 0 && xOffset == -1) {return -1 ;}
        //encore
        if (index %width == width-1 && xOffset == 1) {return -1 ;}
        // Return -1 if out of bounds
        if (newIndex < 0 || newIndex >= maxIndex ) {
            return -1;
        }

        return newIndex; // Return valid index
    }

    public RectangularMap(int longu, int larg)
    {
        width = larg;
        height = longu;
    }

    public String getSize() {return ""+width+""+ height ;}

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

}

