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


        // on cherche à savoir si les tables voisines de l'etu on la meme classe
        // donc on parcourt les voisins
        for (Student s : etu)
        {
            if (s != null && student.sameGroup(s, subGroup))
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