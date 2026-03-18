package org.NeoMalokVector.SAE_S3;

import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class Room
{

    private final CreatingIntermediate creating;
    private PositioningIntermediate positioning;
    public String debugMessage;

    private boolean generated;


    public Room() throws FileNotFoundException
    {
        creating = new CreatingIntermediate();
        positioningMode();
    }

    public Room(String path) throws FileNotFoundException
    {
        creating = new CreatingIntermediate(path);
        positioningMode();
    }

    public CreatingIntermediate getCreating()
    {
        return creating;
    }

    public PositioningIntermediate getPositioning()
    {
        return positioning;
    }

    public void positioningMode() {
        positioning = creating.generatePositioning();}


    public boolean generate()
    {
        generated = positioning.creerPlacement();
        return generated;
    }

    public boolean swapPlaces(int num1, int num2)
    {
        if (positioning != null)
        {
            return positioning.swapPlaces(num1, num2);
        }
        return false;
    }

    public boolean isGenerated() {
        return generated;
    }
}
