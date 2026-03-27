package placement;

import utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends Map {
    private final int width;
    private final int height;

    public RectangularMap(int width, int height) {
        this.width = height;
        this.height = width;
    }

    @Override
    public int[] neighbours(int table, int[] available) {
        int position = Utilitaire.indexOf(table, available);

        int[][] offsets = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

        List<Integer> validNeighbours = new ArrayList<>();

        for (int[] offset : offsets) {
            int neighborIndex = getNeighbour(position, offset[0], offset[1], available.length);
            if (neighborIndex != -1)
                validNeighbours.add(available[neighborIndex]);
        }

        return validNeighbours.stream().mapToInt(Integer::intValue).toArray();
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

