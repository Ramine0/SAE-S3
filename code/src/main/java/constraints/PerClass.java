package constraints;

import org.NeoMalokVector.SAE_S3.Student;

public class PerClass extends Constraint
{
    private static boolean subGroup = false;

    public PerClass(boolean sg)
    {
        subGroup=sg;
    }

    @Override
    public boolean validate(Student student, int table, Student[] etu)
    {
        for (Student voisin : etu)
        {
            if (voisin != null && student.sameGroup(voisin, subGroup))
            {
                return false;
            }
        }

        return true;

    }

    public String typePerClass(){
        if (subGroup){
            return "sub-group";
        }
        return "group";
    }
    public String toDatabase(){
        return "C,"+subGroup;
    }
}