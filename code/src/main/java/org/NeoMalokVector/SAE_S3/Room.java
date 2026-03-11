package org.NeoMalokVector.SAE_S3;

import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class Room
{

    private CreatingIntermediate crea;
    private PositioningIntermediate posing;
    public String debugMessage;

    private boolean generated;


    public Room() throws FileNotFoundException
    {
        crea = new CreatingIntermediate();
        positioningMode();
    }

    public Room(String path) throws FileNotFoundException
    {
        crea = new CreatingIntermediate(path);
        positioningMode();
    }

    public CreatingIntermediate getCrea()
    {
        return crea;
    }

    public PositioningIntermediate getPositioningIntermediate()
    {
        return posing;
    }

    public void positioningMode() {posing = crea.generatePos();}


    public boolean generate()
    {
        generated = posing.creerPlacement();
        return generated;
    }

    public boolean swapPlaces(int num1, int num2)
    {
        if (posing != null)
        {
            return posing.swapPlaces(num1, num2);
        }
        return false;
    }

    public boolean isGenerated() {
        return generated;
    }
}
