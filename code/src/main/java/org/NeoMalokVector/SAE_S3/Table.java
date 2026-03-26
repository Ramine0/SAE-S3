package org.NeoMalokVector.SAE_S3;

public class Table {
    private static int id;

    private final int cordX;
    private final int cordY;

    private int number;
    private Student student;

    /**
     * Constructeur par défaut de table, crée une table à la suite de la dernière table
     */
    public Table() {
        id++;
        number = id;

        cordX = -1;
        cordY = -1;
    }

    public Table(int x, int y) {
        cordX = x;
        cordY = y;

        id++;
        number = id;
    }

    public Table(int number, int x, int y) {
        this.number = number;

        cordX = x;
        cordY = y;

        id = number;
    }

    public Table(int number, int x, int y, Student s) {
        this.number = number;

        cordX = x;
        cordY = y;

        student = s;
        id = number;
    }

    public static void reset() {
        id = 0;
    }

    /**
     * Fonction qui permet d'afficher les informations des tables dans le plan
     *
     * @return les informations de la table, soit son id si pas d'étudiant, soit son id+student.id si étudiant
     */
    public String describe() {
        String description = number + "!";

        if (cordX != -1)
            description += cordX + "!" + cordY + "!";

        description += student != null ? student.getFullName() : "aucun étu";

        return description;
    }

    public String getInformations() {
        String information = "" + number;

        if (cordX != -1)
            information += "!" + cordX + "!" + cordY;

        if (student != null)
            information += "!" + student.getId();

        return information;
    }

    /**
     * Getter du numéro de table
     *
     * @return num
     */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int[] getCoordinates() {
        return new int[] { cordX, cordY };
    }
}
