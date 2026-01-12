package org.NeoMalokVector.SAE_S3;

import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class Room
{

    private CreatingIntermediate crea ;
    private PositioningIntermediate posing;
    public String message ;


    public Room() throws FileNotFoundException
    {
        crea = new CreatingIntermediate();
        posing = null ;
    }

    public Room(String path) throws FileNotFoundException
    {
        crea = new CreatingIntermediate(path);

    }
    public CreatingIntermediate getCrea() {
        return crea;
    }
    public PositioningIntermediate getPositioningIntermediate() {
        return posing;
    }

    public boolean positioningMode() {
        if (crea != null) {
            posing = crea.generatePos();
            if (posing != null) {
                crea = null;
                return true;
            }
        }else return posing != null;
        return false ;
    }

    public boolean generate() {
        return posing.creerPlacement() ;
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
    public boolean swapPlaces(int num1,  int num2) {
        if (posing != null) {
            return posing.swapPlaces(num1,num2) ;
        }
        return false ;
    }

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

    public String describeData () {
        if (posing == null) {
            return "posing null" ;
        }
        return posing.descripData() ;
    }

    public String descripPlaces() {
        if (posing == null) {
            return "posing null" ;
        }
        return posing.getTablesInfoForVisu() ;
    }

}
