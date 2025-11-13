package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public abstract class Constraint
{
    public static String[] groupsConstraints;
    public static Student[] studentsConstraints;

    public abstract boolean validate(Student student, Table table, Table[] tables);
    public abstract boolean contraint(Student student);
}
