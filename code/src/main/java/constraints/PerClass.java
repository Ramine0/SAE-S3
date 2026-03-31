package constraints;

import org.NeoMalokVector.SAE_S3.Student;

public class PerClass extends Constraint {
    private static boolean subgroup;

    public PerClass(boolean subgroup) {
        PerClass.subgroup = subgroup;
    }

    @Override
    public boolean validate(Student student, int tableNumber, Student[] students) {
        // on cherche à savoir si les tables voisines de l'étudiant ont la même classe
        // donc on parcourt les voisins
        for (Student s : students)
            if (s != null && student.sameGroup(s, subgroup))
                return false;

        return true;
    }

    public String typePerClass() {
        return subgroup ? "sub-group" : "group";
    }

    public String toDatabase() {
        return "C," + subgroup;
    }
}