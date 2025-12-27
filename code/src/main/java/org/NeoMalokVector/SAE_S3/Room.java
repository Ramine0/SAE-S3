package org.NeoMalokVector.SAE_S3;

import jdk.jshell.execution.Util;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

public class Room
{

    private CreatingIntermediate crea ;
    private PositioningIntermediate positioningIntermediate;


    public Room() {
        crea = new CreatingIntermediate();

    }

    public Room(String path) {
        crea = new CreatingIntermediate(path);

    }
    public CreatingIntermediate getCrea() {
        return crea;
    }
    public PositioningIntermediate getPositioningIntermediate() {
        return positioningIntermediate;
    }
    private void createRoom() {

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
    private void swapPlaces(int num1,  int num2) {}

    /**
     * Retire la table num des tables auxquelles un étudiant peut être placé.
     * @param num numéro de la table retirée
     */
    private void deleteTable(int num) {

    }

    /**
     * Exporte le placement sous les différents formats demandés.
     * !!! Il faudra rajouter un moyen de choisir les différents formats d'export (3 booléens ou un string) !!!
     */
    private void export(String opt)
    {
        if (opt.length()==3){
            //on exporte l'excel, le plan et le listing
        }else if  (opt.length()==2){
            if (opt.equals("EP")){
            // on exporte l'excel et le plan
            }else if (opt.equals("EL")){
            // on exporte l'excel et le listing
            }else{
            //on exporte le plan et le listing
            }

        }else if (opt.length()==1){
            if (opt.equals("E")){
            // on exporte l'excel
            }else if (opt.equals("P")){
            // on exporte le plan
            }else{
            // on exporte le listing
            }
        }
    }
}
