package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

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

        if (Utilitaire.in(student.getClass(), groupsConstraints.toArray()))
        {
            // on cherche a savoir si les tables voisines de l'etu on la meme classe
            // donc on parcoure les voisins
            for (Student s : etu)
            {
                if (student.sameGroup(s, subGroup))
                {
                    return false;
                }
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


}
