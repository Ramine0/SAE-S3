package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public abstract class Constraint
{
    public static ArrayList<String> groupsConstraints = new ArrayList<>();
    public static ArrayList<Student> studentsConstraints = new ArrayList<>();

    public abstract boolean validate(Student student, int table, Student[] etu);

    public static boolean contraint(Student student)
    {
        return Utilitaire.in(student, studentsConstraints.toArray());
    }
}
