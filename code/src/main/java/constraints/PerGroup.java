package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

public class PerGroup extends Constraint
{
    private String[] groupe ;
    private PerGroup(String[] etus) {groupe = etus ;}
    @Override
    public boolean validate(Student student, Table table, Student[] etu)
    {
        if (Utilitaire.in(student, studentsConstraints)){
            for (Student s : etu) {
                if (Utilitaire.in(s, studentsConstraints)) {
                    return false;
                }
            }
        }
        return true;
    }

}
