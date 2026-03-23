package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.Random;

public class PositioningIntermediate {
    private final Data donnees;

    private final Random random = new Random();

    public PositioningIntermediate(Data d) {
        donnees = d;
    }

    public boolean creerPlacement() {
        donnees.placeImposedStudents();

        int i;
        int tableNumber = 0;

        while (!isGenerationDone(tableNumber)) {
            tableNumber++;

            if (!Utilitaire.in(tableNumber, donnees.freeTables()))
                continue;

            String[] freeStudents = donnees.freeStudents();
            String studentId = freeStudents[random.nextInt(freeStudents.length)];

            i = 0;

            // si on peut pas placer l'étudiant d'id studentId
            while (!walid(donnees.getStudentFromId(studentId), tableNumber)) {
                // on teste un autre étudiant
                studentId = freeStudents[random.nextInt(freeStudents.length)];

                // on compte le nombre d'itérations de la boucle
                i++;

                // on passe à la table suivante si on a fait trop d'itérations
                if (i > freeStudents.length / 2)
                    tableNumber++;
            }

            donnees.placeStudent(tableNumber, studentId);
        }

        return donnees.freeStudents().length == 0;
    }

    private boolean isGenerationDone(int tableNumber) {
        return donnees.freeStudents().length == 0 || tableNumber > donnees.maxTableID();
    }

    // valide ou non le placement
    private boolean walid(Student s, int t) {
        System.out.println(s.getFullName());

        if (!Utilitaire.in(t, donnees.freeTables()))
            return false;

        // si on sait que l'étudiant a des contraintes
        if (Constraint.contraint(s.getId()) || donnees.hasMode()) {
            // on prend les tables voisines pour regarder
            Student[] voisins = donnees.neighbours(t);

            // on vérifie si ça bloque
            for (Constraint c : donnees.getConstraints())
                if (c != null && !c.validate(s, t, voisins))
                    return false; // ça bloque
        }

        // sinon tout est bon à moins que la place soit déjà prise
        return true;
    }

    public String getTablesForVisu() {
        StringBuilder result = new StringBuilder(donnees.getPlanSize() + "/");

        for (int t : donnees.existingTables())
            if (!donnees.isDeleted(t))
                result.append(donnees.getTableInfos(t)).append(";");

        return result.toString();
    }

    public boolean swapPlaces(int numT1, int numT2) {
        if (Utilitaire.in(numT1, donnees.existingTables()) && Utilitaire.in(numT2, donnees.existingTables()))
            return donnees.swap(numT1, numT2);

        return false;
    }

    public String describeData() {
        StringBuilder result = new StringBuilder();

        for (String s : donnees.describe())
            result.append(s).append(";");

        return result.toString();
    }

    public String tabInfoForVisu(int nb) {
        return donnees.getInformationsForVisualisation(nb);
    }
}