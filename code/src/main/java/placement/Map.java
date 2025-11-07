package placement;

import org.NeoMalokVector.SAE_S3.Table;

public abstract class Map
{
    public abstract Table[] neighbours(Table table, Table[] tables);
}
