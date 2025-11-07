package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class ImposedPlacement extends Constraint
{
    public Table table;
    public Student student;

    @Override
    public boolean validate(Table table, Table[] tables)
    {
        if (table.student == null)
        {
            table.student = student;

            return true;
        }

        return false;
    }
}
