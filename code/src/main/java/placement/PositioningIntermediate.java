package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public class PositioningIntermediate {
    public Map map;

    public Table[] deletedTables;
    public Constraint[] constraints;
    public Table[] tables;
    public Student[] students;

    public void CreerPlacement () {}

    // Ici constructeur de l'intermediaire il prends en paramettre une sting qui donne les infos du format de plan
    // charAt(0) c le type (rectangle) et les 2 suivants c l et L (pour rect)
    // on donne aussi les numero de tables supprimées
    // on donne pas le fichier d'etu car comme il y en a qu'1 on saura deja comment et ou on va l'enregistrer
    // on va lme lire ici MAIS il faudra pour ca le save qqp AVANT
    public PositioningIntermediate(String mapType, int[] deleted) {

        if (mapType.charAt(0) == 'R') {
            // plan rectangulaire
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)), Character.getNumericValue(mapType.charAt(2)));
        }

    }

    // valide ou non le placement
    private boolean walid (Student s, Table t){


        // si on sait que l'etu as des contraintes
        if (Constraint.contraint(s)) {

            // on prends les tables voisines pour regarder
            Table[] voisins = neighbours(t) ;
            for (Constraint c : constraints) {
                // si ca bloque
                if (! c.validate(s,t,voisins)) {
                    return false; // ca bloque
                }
            }

        }
        // sinon tout est ok
        return true ;
    }

    // je prends les voisins de ma table
    private Table[] neighbours(Table t) {
        ArrayList<Table> voisins = new ArrayList<>() ;

        // pour tous les voisins de la map
        for (int i : map.neighbours(t.getNum(), numDispo() )) {
            //je recupere la table
            voisins.add(tableFromNumber(i));
        }

        return voisins.toArray(new Table[0])  ;
    }

    // je recupere la table via son numero
    private Table tableFromNumber(int nb) {
        //je cherche la table dans la liste...
        for (Table t : tables ) {
            if (t.getNum() == nb) {
                return t;
            }
        }
        return null ;
    }

    // je cherche tous les numeros de tables valide (pas supprimées)
    private int[] numDispo() {
        int [] result = new int [tables.length - deletedTables.length];
        for (int i = 0; i < tables.length ; i++ ) {
            // je verifie que ma table soit pas supprimée
            if (! Utilitaire.in(tables[i],deletedTables)) {
                // si c ok je l'ajoute a la liste
                result[i] = tables[i].getNum();
            }
        }
        return  result ;
    }

}