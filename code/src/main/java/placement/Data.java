package placement;


import constraints.Constraint;
import constraints.ImposedPlacement;
import constraints.PerClass;
import jdk.jshell.execution.Util;
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
    int[] freeTables()  revoie un tableau de num de tables disponibles FAIT
    getTable(int) FAIT
    getDeleted() renvoie le tableau FAIT
    freeStudents() renvoie les etus qui sont pas deja placés FAIT
    removeTable() FAIT
    unremoveTable() FAIT
    findStudent(string id) ;


     */
    public boolean isDeleted(int numTable){
        return (Utilitaire.in(numTable, deletedTables));
    }
    public Table getTable(int numTable){
        return tables[numTable];
    }
    public int[] getDeleted(){
        return deletedTables;
    }
    public String[] freeStudents(){
        String[] place=new String[students.length];
        int numPla=0;
        for (int i=0; i<tables.length; i++){
            if (tables[i].getEtu()!=null){
                place[numPla]=tables[i].getEtu().getId();
                numPla++;
            }
        }
        String[] rest=new String[numPla];
        int numRest=0;
        for (int i=0; i<tables.length; i++){
            if (!Utilitaire.in(students[i].getId(),place)){
                rest[numRest]=tables[i].getEtu().getId();
                numRest++;
            }
        }
        return rest;
    }
    public Student getStuFromTab(int num) {
        return tables[num].getEtu();
    }

    // renvoie les numeros de tables disponibles
    public int[] existingTables() {
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

    public int[] freeTables(){
        int [] free=new int [tables.length - deletedTables.length];
        int numRes = 0 ;
        for (int i=0; i<tables.length; i++){
            if (Utilitaire.in(i, existingTables()) && tables[i].getEtu()==null){
                free[numRes] = i;
                numRes++;
            }
        }
        return free;
    }

    public void removeTable(int num) {
        int i=tables.length-existingTables().length-1;
        deletedTables[i]=num;
    }
    public void unremoveTable(int num){
        for (int n: deletedTables){
            if (deletedTables[n]==num){
                deletedTables[n]=-1;
            }
        }
    }
    public Constraint[] getConstr() {return constraints;}
    public int[] getTables() {
        int [] lesNums = new int[tables.length] ;
        for (int i = 0 ; i < tables.length ; i ++ ) {
            lesNums[i] =  tables[i].getNum();
        }
        return lesNums ;
    }
    public Student findStudent(String idStudent){
        return null;
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
