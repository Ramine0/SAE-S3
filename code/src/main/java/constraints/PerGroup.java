package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

public class PerGroup extends Constraint
{
    private String[] groupe ;
    public PerGroup(String[] etus) {groupe = etus ;}
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
        /*
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

         */
    }

}
