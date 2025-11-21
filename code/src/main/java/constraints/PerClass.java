package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

public class PerClass extends Constraint
{
    static boolean subGroup = false ;

    @Override
    public boolean validate(Student student, Table table, Student[] etu) {

        if (Utilitaire.in(student.getClass(),groupsConstraints)) {
            // on cherche a savoir si les tables voisines de l'etu on la meme classe
            // donc on parcoure les voisins
            for (Student s : etu) {
                if (student.sameGroup(s, subGroup)) {
                    return false;
                }
            }
        }
        return true ;

    }



}
