package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public abstract class Constraint
{
    public static String[] groupsConstraints;
    public static Student[] studentsConstraints;

    public abstract boolean validate(Table table, Table[] tables);
}
