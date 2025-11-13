package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class PerGroup extends Constraint
{
    @Override
    public boolean validate(Student student, Table table, Table[] tables)
    {
        if (!contraint(student)){
            return true;
        }else{
            for (Table t : tables){
                if (t.student!=null && contraint(t.student)){
                    return false;
                }
            }
            return true;
        }
    }

}
