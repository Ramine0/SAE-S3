package org.NeoMalokVector.SAE_S3;

public class Table
{
    private static int id=0;
    private int num;
    private Student student;
    private final int coordX;
    private final int coordY ;

    /**
     * Constructeur par défaut de table, crée une table à la suite de la dernière table
     */
    public Table(){
        id++;
        num=id;
        coordX = -1;
        coordY = -1;
    }

    public Table(int  x, int y) {
        coordX = x ;
        coordY = y;
        id++ ;
        num = id ;
    }

    public Table(int number, int x, int y, Student s){
        num = number;
        coordX = x;
        coordY = y;
        student = s;
    }
    /**
     * Fonction qui permettra d'afficher les informations des tables dans le plan
     * @return les informations de la table, soit son id si pas d'étudiant, soit son id+student.id si étudiant
     */
    public String description()
    {
        String result =num+"!";
        if (coordX != -1) {
            result +=  coordX+"!"+coordY+"!";
        }
        if (student!=null){
           result += student.getFullName();
        }else {
            result += "aucun etu";
        }
        return result ;
    }

    public String info(){
        String result=""+num;
        if (coordX != -1) {
            result +=  "!"+coordX+"!"+coordY;
        }
        if (student!=null){
            result +=  "!"+student.getId();
        }
        return result;
    }

    /**
     * getter du numéro de table
     * @return num
     */
    public int getNum() {
        return num;
    }
    public void setNum(int num){
        this.num=num;
    }


    public Student getEtu () {return student ;}



    public void setStudent(Student student) {this.student = student;}

    public static void reset()
    {
        id = 0;
    }

    public int[] getCoord () {
        return new int[]{coordX,coordY} ;
    }
}
