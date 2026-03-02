package org.NeoMalokVector.SAE_S3;

import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class Room
{

    private CreatingIntermediate crea;
    private PositioningIntermediate posing;
    public String message; // c pour debug

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


    /**
     * Fonction qui change la place de l'étudiant à la table num1 avec l'étudiant à la table num2.
     * S'il n'y a pas d'étudiant à l'une des tables, change juste la place d'un étudiant.
     * Il faudrait peut-être voir si on ne la déplace pas dans PositioningIntermediate, vu que c'est
     * ce qui est censé permettre de placer les élèves et que la fonction de création de placement est dans PositioningIntermediate.
     * PositioningIntermediate n'a pas d'accès à swapPlaces au passage.
     *
     * @param num1 numéro de la première table
     * @param num2 numéro de la deuxième table
     */
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
