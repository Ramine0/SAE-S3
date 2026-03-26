package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.Random;

public class PositioningIntermediate {
    private final Data data;

    private final Random random = new Random();

    public PositioningIntermediate(Data d) {
        data = d;
    }

    public boolean creerPlacement() {
        data.placeImposedStudents();

        int i;
        int tableNumber = 0;

        while (!isGenerationDone(tableNumber)) {
            tableNumber++;

            if (!Utilitaire.in(tableNumber, data.freeTables()))
                continue;

            String[] freeStudents = data.freeStudents();
            String studentId = freeStudents[random.nextInt(freeStudents.length)];

            i = 0;

            while (!walid(data.getStudentFromId(studentId), tableNumber)) {
                studentId = freeStudents[random.nextInt(freeStudents.length)];

                i++;

                if (i > freeStudents.length / 2)
                    tableNumber++;
            }

            data.placeStudent(tableNumber, studentId);
        }

        return data.freeStudents().length == 0;
    }

    private boolean isGenerationDone(int tableNumber) {
        return data.freeStudents().length == 0 || tableNumber > data.maxTableID();
    }

    private boolean walid(Student student, int table) {
        if (!Utilitaire.in(table, data.freeTables()))
            return false;

        if (Constraint.contain(student.getId()) || data.hasMode()) {
            Student[] neighbours = data.neighbours(table);

            for (Constraint c : data.getConstraints())
                if (c != null && !c.validate(student, table, neighbours))
                    return false;
        }

        return true;
    }

    public String getTablesForVisu() {
        StringBuilder result = new StringBuilder(data.getPlanSize() + "/");

        for (int t : data.existingTables())
            if (!data.isDeleted(t))
                result.append(data.getTableInfos(t)).append(";");

        return result.toString();
    }

    public boolean swapPlaces(int numT1, int numT2) {
        if (Utilitaire.in(numT1, data.existingTables()) && Utilitaire.in(numT2, data.existingTables()))
            return data.swap(numT1, numT2);

        return false;
    }

    public String describeData() {
        StringBuilder result = new StringBuilder();

        for (String s : data.describe())
            result.append(s).append(";");

        return result.toString();
    }

    public String tabInfoForVisu(int nb) {
        return data.getInformationsForVisualisation(nb);
    }
}