package constraints;

import org.the_disabled.sae_s3.Student;

public class ImposedPlacement extends Constraint {
    private final int tableNumber;
    private final String studentNumber;

    public ImposedPlacement(int tableNumber, String studentNumber) {
        this.tableNumber = tableNumber;
        this.studentNumber = studentNumber;

        studentsConstraints.add(studentNumber);
    }

    @Override
    public boolean validate(Student student, int tableNumber, Student[] students) {
        return (student.getId().equals(studentNumber) && tableNumber == this.tableNumber);
    }

    /**
     * Fonction permettant d'obtenir l'étudiant avec sa table. On devrait l'utiliser plus tard au début de la génération du placement
     *
     * @return un tableau de string contenant le numéro de la table et le numéro de l'étudiant
     */
    public String[] getImposed() {
        return new String[]{Integer.toString(tableNumber), studentNumber};
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String toDatabase() {
        return "I," + studentNumber + "," + tableNumber;
    }

}
