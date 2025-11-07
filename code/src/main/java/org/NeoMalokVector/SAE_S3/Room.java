package org.NeoMalokVector.SAE_S3;

import placement.PositioningIntermediate;

public class Room
{
    private PositioningIntermediate positioningIntermediate;

    public Room()
    {
        this.positioningIntermediate = new PositioningIntermediate();
    }

    private void createRoom()
    {
    }

    private void swapPlaces(int num1,  int num2)
    {
        Table temp = positioningIntermediate.tables[num1];
        positioningIntermediate.tables[num1] = positioningIntermediate.tables[num2];
        positioningIntermediate.tables[num2] = temp;
    }

    private void deleteTable(int num)
    {
        positioningIntermediate.tables[num] = null;
    }

    private void export()
    {

    }
}
