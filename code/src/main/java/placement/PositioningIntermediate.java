package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.Random;

public class PositioningIntermediate {
    private final Data data;
    private final Random random = new Random();

    public PositioningIntermediate(Data data) {
        this.data = data;
    }

    public boolean creerPlacement() {
        data.placeImposedStudents();

        int i;
        int tableNumber = 0;

        int freeStudentsNumber = -1;

        while (!isGenerationDone(tableNumber)) {
            tableNumber++;

            if (!Utilitaire.in(tableNumber, data.freeTables()))
                continue;

            String[] freeStudents = data.freeStudents();
            String studentId = freeStudents[random.nextInt(freeStudents.length)];

            if (freeStudentsNumber == -1)
                freeStudentsNumber = freeStudents.length;

            i = 0;

            while (!validateStudentPlacement(data.getStudentFromId(studentId), tableNumber)) {
                studentId = freeStudents[random.nextInt(freeStudents.length)];

                i++;

                if (i > freeStudentsNumber / 2)
                    tableNumber++;
            }

            data.placeStudent(tableNumber, studentId);
        }

        return data.freeStudents().length == 0;
    }

    private boolean isGenerationDone(int tableNumber) {
        return data.freeStudents().length == 0 || tableNumber > data.maxTableID();
    }

    private boolean validateStudentPlacement(Student student, int tableNumber) {
        if (!Utilitaire.in(tableNumber, data.freeTables()))
            return false;

        if (Constraint.contraint(student.getId()) || data.hasMode()) {
            Student[] neighbours = data.neighbours(tableNumber);

            for (Constraint c : data.getConstraints())
                if (c != null && !c.validate(student, tableNumber, neighbours))
                    return false;
        }

        return true;
    }

    public String getTablesForVisualisation() {
        StringBuilder result = new StringBuilder(data.getPlanSize() + "/");

        for (int table : data.existingTables())
            if (!data.isDeleted(table))
                result.append(data.getTableInfos(table)).append(";");

        return result.toString();
    }

    public boolean swapPlaces(int table1Number, int table2Number) {
        if (Utilitaire.in(table1Number, data.existingTables()) && Utilitaire.in(table2Number, data.existingTables()))
            return data.swap(table1Number, table2Number);

        return false;
    }

    public String describeData() {
        StringBuilder result = new StringBuilder();

        for (String student : data.describe())
            result.append(student).append(";");

        return result.toString();
    }

    public String tableInfoForVisualisation(int tableNumber) {
        return data.getInformationsForVisualisation(tableNumber);
    }
}