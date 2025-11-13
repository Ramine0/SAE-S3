package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

public abstract class Constraint
{
    public static String[] groupsConstraints;
    public static Student[] studentsConstraints;

    public abstract boolean validate(Student student, Table table, Table[] tables);
    public static boolean contraint(Student student) {return Utilitaire.in(student, studentsConstraints);}
}
