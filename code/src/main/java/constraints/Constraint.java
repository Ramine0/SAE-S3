package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public abstract class Constraint
{
    public static final ArrayList<String> studentsConstraints = new ArrayList<>();

    public abstract boolean validate(Student student, int table, Student[] etu);
    public abstract String toDatabase();
    public static void reset(){
        studentsConstraints.clear();
    }

    public static boolean contraint(String student)
    {
        return Utilitaire.in(student, studentsConstraints.toArray());
    }
}
