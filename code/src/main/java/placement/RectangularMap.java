package placement;

import utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends Map {
    private final int width;
    private final int height;

    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int[] neighbours(int table, int[] dispo) {
        int pos = Utilitaire.pos(table, dispo);

        int[][] offsets = { { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };

        List<Integer> validNeighbors = new ArrayList<>();

        for (int[] offset : offsets) {
            int neighborIndex = getNeighbour(pos, offset[0], offset[1], dispo.length);

            if (neighborIndex != -1)
                validNeighbors.add(dispo[neighborIndex]);
        }

        return validNeighbors.stream().mapToInt(Integer::intValue).toArray();
    }

    private int getNeighbour(int index, int xOffset, int yOffset, int maxIndex) {
        int newIndex = index + xOffset + yOffset * width;

        if (index % width == 0 && xOffset == -1)
            return -1;

        if (index % width == width - 1 && xOffset == 1)
            return -1;

        if (newIndex < 0 || newIndex >= maxIndex)
            return -1;

        return newIndex;
    }

    public String getSize() {
        return width + ";" + height;
    }
}

