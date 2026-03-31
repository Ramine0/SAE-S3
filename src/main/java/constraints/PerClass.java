package constraints;

import org.the_disabled.sae_s3.Student;

public class PerClass extends Constraint {
    private static boolean subgroup;

    public PerClass(boolean subgroup) {
        PerClass.subgroup = subgroup;
    }

    @Override
    public boolean validate(Student student, int tableNumber, Student[] students) {
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