package org.NeoMalokVector.SAE_S3;

public class Table
{
    private static int id=0;
    private int number;
    private Student student;
    private final int cordX;
    private final int cordY;

    /**
     * Constructeur par défaut de table, crée une table à la suite de la dernière table
     */
    public Table(){
        id++;
        number =id;
        cordX = -1;
        cordY = -1;
    }

    public Table(int  x, int y) {
        cordX = x ;
        cordY = y;
        id++ ;
        number = id ;
    }
    public Table(int number, int  x, int y) {
        this.number = number ;
        cordX = x ;
        cordY = y;
        id = number ;
    }

    public Table(int number, int x, int y, Student s){
        this.number = number;
        cordX = x;
        cordY = y;
        student = s;
        id = number ;
    }

    /**
     * Fonction qui permet d'afficher les informations des tables dans le plan
     * @return les informations de la table, soit son id si pas d'étudiant, soit son id+student.id si étudiant
     */
    public String description()
    {
        String description = number +"!";
        if (cordX != -1) {
            description +=  cordX +"!"+ cordY +"!";
        }
        if (student!=null){
           description += student.getFullName();
        }else {
            description += "aucun etu";
        }
        return description ;
    }

    public String information(){
        String information=""+ number;
        if (cordX != -1) {
            information +=  "!"+ cordX +"!"+ cordY;
        }
        if (student!=null){
            information +=  "!"+student.getId();
        }
        return information;
    }

    /**
     * Getter du numéro de table
     * @return num
     */
    public int getNumber() {
        return number;
    }
    public void setNumber(int number){
        this.number = number;
    }


    public Student getStudent() {return student ;}



    public void setStudent(Student student) {
        this.student = student;
    }

    public static void reset()
    {
        id = 0;
    }

    public int[] getCoordinates() {
        return new int[]{cordX, cordY} ;
    }
}
