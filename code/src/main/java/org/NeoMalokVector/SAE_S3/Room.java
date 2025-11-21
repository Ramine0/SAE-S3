package org.NeoMalokVector.SAE_S3;

import jdk.jshell.execution.Util;
import placement.PositioningIntermediate;

public class Room
{
    private PositioningIntermediate positioningIntermediate;

    public Room()
    {
        this.positioningIntermediate = new PositioningIntermediate("R00",null);
    }

    private void createRoom()
    {

    }

    /**
     * Fonction qui change la place de l'étudiant à la table num1 avec l'étudiant à la table num2.
     * S'il n'y a pas d'étudiant à l'une des tables, change juste la place d'un étudiant.
     * Il faudrait peut-être voir si on la déplace pas dans PositioningIntermediate, vu que c'est
     * ce qui est censé permettre de placer les élèves et que la fonction de création de placement est dans PositioningIntermediate.
     * PositioningIntermediate a pas d'accès à swapPlaces au passage.
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
    private void deleteTable(int num) {
        positioningIntermediate.tables[num].student = null;
        for (Table table : positioningIntermediate.tables){
            if (num == table.getNum()) {
                positioningIntermediate.deletedTables[num] = table;
            }
        }
    }

    /**
     * Exporte le placement sous les différents formats demandés.
     * !!! Il faudra rajouter un moyen de choisir les différents formats d'export (3 booléens ou un string) !!!
     */
    private void export(String opt)
    {
        if (opt.length()==3){

        }else if  (opt.length()==2){
            if (opt.equals("EP")){

            }else if (opt.equals("EL")){

            }else{

            }

        }else if (opt.length()==1){
            if (opt.equals("E")){

            }else if (opt.equals("P")){

            }else{

            }
        }
    }
}
