package org.NeoMalokVector.SAE_S3;

public class Table
{
    private static int id=0;
    public int num;
    public Student student;

    /**
     * Constructeur par défaut de table, crée une table à la suite de la dernière table
     */
    public Table(){
        id++;
        num=id;
    }

    /**
     * Constructeur de la table à utilisée si on ne veut pas rajouter la table à la fin.
     * Exemple: je veut remettre une table supprimée dans les tables.
     * @param num numéro de la table
     */
    public Table(int num){
        this.num=num;
    }

    /**
     * Fonction qui permettra d'afficher les informations des tables dans le plan
     * @return les informations de la table, soit son id si pas d'étudiant, soit son id+studentid si étudiant
     */
    public String description()
    {
        String res=String.valueOf(num);
        if (student!=null){
            return (res+" "+student.id);
        }else{
            return res;
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
     * getter du dernier id des tables
     * @return id
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @param fin
     */
    public void destruction(boolean fin){
        if (fin){
            id--;
        }
    }
}
