package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class PerClass extends Constraint
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

    @Override
    public boolean contraint(Student student) {
        for (String s: groupsConstraints){
            if (s.charAt(0)==student.group){
                return true;
            }else if (s.charAt(2)==student.subGroup){
                return true;
            }
        }
        return false;
    }
}
