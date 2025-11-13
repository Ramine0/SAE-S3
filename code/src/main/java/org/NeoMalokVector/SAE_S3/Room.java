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

    /**
     * Fonction qui change la place de l'étudiant à la table num1 avec l'étudiant à la table num2.
     * S'il n'y a pas d'étudiant à l'une des tables, change juste la place d'un étudiant
     * @param num1 numéro de la première table
     * @param num2 numéro de la deuxième table
     */
    private void swapPlaces(int num1,  int num2)
    {
        Table temp = positioningIntermediate.tables[num1];
        positioningIntermediate.tables[num1].student = positioningIntermediate.tables[num2].student;
        positioningIntermediate.tables[num2].student = temp.student;
    }

    /**
     * Retire la table num des tables auxquelles un étudiant peut être placé.
     * @param num numéro de la table retirée
     */
    private void deleteTable(int num)
    {
        if (num==positioningIntermediate.tables[num].getId()){
            positioningIntermediate.tables[num].destruction(true);
        }

        positioningIntermediate.tables[num] = null;
    }

    /**
     * Exporte le placement sous les différents formats demandés.
     * !!! Il faudra rajouter un moyen de choisir les différents formats d'export (3 booléens ou un string) !!!
     */
    private void export()
    {

    }
}
