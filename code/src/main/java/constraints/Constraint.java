package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public abstract class Constraint {
    public static final ArrayList<String> studentsConstraints = new ArrayList<>();

    public static void reset() {
        studentsConstraints.clear();
    }

    public static boolean contraint(String student) {
        return Utilitaire.in(student, studentsConstraints.toArray());
    }

    public abstract boolean validate(Student student, int tableNumber, Student[] students);

    public abstract String toDatabase();
}
