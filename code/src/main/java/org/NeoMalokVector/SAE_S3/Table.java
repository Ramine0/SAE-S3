package org.NeoMalokVector.SAE_S3;

public class Table
{
    private static int id=0;
    private int num;
    private Student student;

    /**
     * Constructeur par défaut de table, crée une table à la suite de la dernière table
     */
    public Table(){
        id++;
        num=id;
    }

    /**
     * Fonction qui permettra d'afficher les informations des tables dans le plan
     * @return les informations de la table, soit son id si pas d'étudiant, soit son id+student.id si étudiant
     */
    public String description()
    {
        if (student!=null){
            return (num+" "+student.getId());
        }else{
            return String.valueOf(num);
        }
    }

    /**
     * getter du numéro de table
     * @return num
     */
    public int getNum() {
        return num;
    }

    /**
     * getter de l'id maximal attribué à une table
     * @return id
     */
    public int getId(){
        return id;
    }
    public Student getStudent(){
        return student;
    }
    public void setStudent(Student student){
        student=student;
    }
}
