package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public abstract class Constraint
{
    public static ArrayList<String> groupsConstraints = new ArrayList<>();
    public static ArrayList<String> studentsConstraints = new ArrayList<>();

    public abstract boolean validate(Student student, int table, Student[] etu);
    public static void reset(){
        groupsConstraints.clear();
        studentsConstraints.clear();
    }

    public static boolean contraint(String student)
    {
        return Utilitaire.in(student, studentsConstraints.toArray());
    }
}
