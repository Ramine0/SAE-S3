package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

public class PerGroup extends Constraint
{
    private String[] groupe;

    private PerGroup(String[] etus)
    {
        groupe = etus;
    }

    @Override
    public boolean validate(Student student, int table, Student[] etu)
    {
        if (Utilitaire.in(student, studentsConstraints.toArray()))
        {
            for (Student s : etu)
            {
                if (Utilitaire.in(s, studentsConstraints.toArray()))
                {
                    return false;
                }
            }
        }
        return true;
    }

}
