package placement;


import constraints.Constraint;
import constraints.ImposedPlacement;
import constraints.PerClass;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileReader;
import java.util.Scanner;

// la classe va save les données pour les creer via un CreatingIntermediate et les avoir dans le positioning intermediate
// elle aura plein de fonction utiles comme le lecture du fichier des etus ou le traitement des etus

public class Data {

    private Constraint[] constraints;
    private Table[] tables;
    private Student[] students;
    // on laisse utiliser parfaitement les etus car c'est bcp plus des pratique car il y a bcp de traitement a faire
    // notement avec les methodes qui sont assez nombreuses

    private int[] deletedTables;

    public void placeStudent(int table, String idStudent ) {

    }

    // liste des fonctions a implementer
    /*
    bool isDeleted(Table/int) FAIT
    int[] freeTables()  revoie un tableau de num de tables disponibles
    getTable(int)
    getDeleted() renvoie le tableau
    freeStudents() renvoie les etus qui sont pas deja placés
    removeTable()
    unremoveTable()
    findStudent(string id) ;


     */
    public boolean isDeleted(int numTable){
        return (Utilitaire.in(numTable, deletedTables));
    }

    public Student getStuFromTab(int num) {
        return tables[num].getEtu();
    }

    // renvoie les numeros de tables disponibles
    public int[] freeTables() {
        int [] result = new int [tables.length - deletedTables.length];
        int numRes = 0 ; // la position dans les resultats
        for (int i = 0; i < tables.length ; i++) {
            // je verifie que ma table soit pas supprimée
            if (! Utilitaire.in(i,deletedTables)) {
                // si c ok je l'ajoute a la liste
                result[numRes] = i;
                numRes++;
            }
        }
        return  result ;
    }


    public Constraint[] getConstr() {return constraints;}
    public int[] getTables() {
        int [] lesNums = new int[tables.length] ;
        for (int i = 0 ; i < tables.length ; i ++ ) {
            lesNums[i] =  tables[i].getNum();
        }
        return lesNums ;
    }
    public Student[] getEtus() {return students;}

    // le chargement du fichier exel donné par le/la prof
    private boolean chargerFichier () {
        try {
            Scanner sc = new Scanner(new FileReader("../../webapp/resources/etudiants.csv"));
            String[] line ;
            // je pense que si l'ordre varie on va faire des shortInt pour enregistrer la position de chaque element
            // par exemple le prenom en 3 -> prenom = line[indexPrenom]
            // le plus dur serait alors de determiner la position des elements

            while  (sc.hasNextLine()) {
                line = sc.nextLine().split(";");



            }



            return true ;
        } catch (Exception e) {
            return false;
        }

    }

}
