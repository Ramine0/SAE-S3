package placement;

import constraints.Constraint;
import constraints.ImposedPlacement;
import constraints.PerClass;
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

    public void CreerPlacement () {
        if (constraints.length!=0){
            for (Student student : students){
                for (Constraint constraint : constraints) {
                    if (constraint instanceof ImposedPlacement && student.getId().equals(((ImposedPlacement) constraint).numEtu)){
                        changePlace(student, ((ImposedPlacement) constraint).numTable);
                    }else if(Utilitaire.in(student, Constraint.studentsConstraints) || Utilitaire.in(student.getClass(), Constraint.groupsConstraints)){
                        for (Table table : tables) {
                            if (constraint.validate(student, table, neighbours(table)) && !Utilitaire.in(table, deletedTables)){
                                changePlace(student, table.getNum());
                            }
                        }
                    }
                }
            }

        }else{
            int numTable = 0;
            for (Student student : students) {
                numTable++;
                changePlace(student, numTable);
            }
        }
    }

    public void changePlace(Student student, int numTable) {
        for (Table table : tables){
            if (table.getNum()==numTable){
                table.student=student;
            }
        }
    }

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
            Student[] voisins = neighbours(t) ;
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
    private Student[] neighbours(Table t) {
        ArrayList<Student> voisins = new ArrayList<>() ;
        // pour tous les voisins de la map
        for (int i : map.neighbours(t.getNum(), numDispo() )) {

            //je recupere l'etu de la table si on a bien une table
            if (i != -1) {voisins.add(tableFromNumber(i).student);}
        }

        return voisins.toArray(new Student[0])  ;
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