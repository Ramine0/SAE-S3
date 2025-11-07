package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class PositioningIntermediate
{
    public Map map;

    public Table[] deletedTables;
    public Constraint[] constraints;
    public Table[] tables;
    public Student[] students;

    public boolean validatePlacement(Table table, int studentNumber)
    {
        return false;
    }

    public void imposePlaces(Table table, Student student)
    {
        table.student = student;
    }
}
