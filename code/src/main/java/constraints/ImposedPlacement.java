package constraints;

import org.NeoMalokVector.SAE_S3.Student;

public class ImposedPlacement extends Constraint
{
    private final int numTable;
    private final String numStudent;

    public ImposedPlacement(int numTable, String numStudent){
        this.numTable = numTable;
        this.numStudent = numStudent;

        studentsConstraints.add(numStudent);
    }

    // pas besoin de valider, on doit juste placer l'etu a la place qu'on lui a donné
    @Override
    public boolean validate(Student student, int table, Student[] students) {
        return (student.getId().equals(numStudent) && table==numTable) ;
    }

    /**
     * Fonction permettant d'obtenir l'étudiant avec sa table. On devrait l'utiliser plus tard au début de la génération du placement
     * @return un tableau de string contenant le numéro de la table et le numéro de l'étudiant
     */
    public String[] getImposed() {
        return new String[] {Integer.toString(numTable), numStudent} ;
    }

    public int getNumTable() {
        return numTable;
    }

    public String getNumStudent() {
        return numStudent;
    }

    public String toDatabase(){
        return "I,"+ numStudent +","+numTable;
    }

}
