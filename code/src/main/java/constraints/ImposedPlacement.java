package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class ImposedPlacement extends Constraint
{
    public int numTable;
    public String numEtu;

    @Override
    public boolean validate(Student student, Table table, Table[] tables)
    {
        if (!contraint(student)){
            return true;
        }else if (table.student == null)
        {
            numTable=table.num;
            numEtu=student.id;
            table.student = student;

            return true;
        }

        return false;
    }


}
