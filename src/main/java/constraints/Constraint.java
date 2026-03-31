package constraints;

import org.the_disabled.sae_s3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public abstract class Constraint {
    public static final ArrayList<String> studentsConstraints = new ArrayList<>();

    public static void reset() {
        studentsConstraints.clear();
    }

    public static boolean contains(String student) {
        return Utilitaire.in(student, studentsConstraints.toArray());
    }

    public abstract boolean validate(Student student, int tableNumber, Student[] students);

    public abstract String toDatabase();
}
