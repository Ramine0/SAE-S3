package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

public class PerGroup extends Constraint {
    private final int number;
    private final String[] group;

    public PerGroup(String student, int groupNumber) {
        number = groupNumber;
        group = new String[9];

        addStudent(student);

    }

    public void addStudent(String studentNumber) {
        int id = -1;

        for (int i = 0; i < group.length; i++)
            if (group[i] == null && id == -1)
                id = i;

        group[id] = studentNumber;
        studentsConstraints.add(studentNumber);
    }

    public void removeStudent(int index) {
        group[index] = null;
    }

    @Override
    public boolean validate(Student student, int table, Student[] neighbours) {
        if (Utilitaire.in(student.getId(), group))
            for (Student s : neighbours) {
                System.out.println(s.getFullName());

                if (Utilitaire.in(s.getId(), group))
                    return false;
            }

        return true;
    }

    public boolean haveStudent(String id) {
        return Utilitaire.in(id, group);
    }

    public String toString() {
        String result = "";

        for (String s : group)
            result = result + s + ";";

        return result;
    }

    public String toDatabase() {
        StringBuilder group = new StringBuilder("G," + number);

        for (String s : this.group)
            group.append(",").append(s);

        return group.toString();
    }

    public int getNumber() {
        return number;
    }
}
