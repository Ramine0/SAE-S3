package placement;

import org.NeoMalokVector.SAE_S3.Table;

import java.util.ArrayList;

public class RectangularMap extends Map
{
    public int width, height;

    @Override
    public Table[] neighbours(Table table, Table[] tables)
    {
        ArrayList<Table> neighbours = new ArrayList<>();

        if (table.num - 1 >= 0)
            neighbours.add(tables[table.num - 1]);
        
        if (table.num + 1 < tables.length)
            neighbours.add(tables[table.num + 1]);

        return neighbours.toArray(new Table[0]);
    }
}
