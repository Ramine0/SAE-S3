package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.Random;

public class PositioningIntermediate {
    private Data donnees; // contient les étudiants et les tables
    // On ne manipule pas directement les tables, on a juste leur numéro. Question d'optimisation et de securité

    private final Random random = new Random();

    // on fait ce qu'on veut des contraintes, c'est plus simple et plus pratique

    public PositioningIntermediate(Data d) {
        donnees = d;
    }

    // À VOIR : pour l'instant ce constructeur est inutile, on va peut-être le supprimer plus tard
    private PositioningIntermediate(String path) { // path contient les informations du format de plan
        // premier caractère : type (R = rectangle)
        // deuxième et troisième caractères : longueur et largeur du rectangle

        // on donne aussi les numéros de tables supprimées


        try {
            donnees = new Data(path, "R");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean creerPlacement() {
        donnees.placerImposes();

        int loopCount;
        int tableNumber = 0;

        while (donnees.freeStudents().length != 0 && tableNumber <= donnees.maxTableID()) {
            tableNumber++;

            if (!Utilitaire.in(tableNumber, donnees.freeTables()))
                continue;

            String[] freeStudents = donnees.freeStudents();
            String studentId = freeStudents[random.nextInt(freeStudents.length)];

            loopCount = 0;
            while (!walid(donnees.getStudentFromId(studentId), tableNumber)) {
                studentId = freeStudents[random.nextInt(freeStudents.length)];

                loopCount++;
                if (loopCount > freeStudents.length / 2)
                    tableNumber++;
            }

            donnees.placeStudent(tableNumber, studentId);
        }

        return donnees.freeStudents().length == 0;
    }

    // valide ou non le placement
    private boolean walid(Student s, int t) {
        if (!Utilitaire.in(t, donnees.freeTables()))
            return false;

        // si on sait que l'étudiant a des contraintes
        if (Constraint.contraint(s.getId()) || donnees.hasMode()) {
            // on prend les tables voisines pour regarder
            Student[] voisins = donnees.neighbours(t);

            for (Constraint c : donnees.getConstr()) {
                // on vérifie si ça bloque
                if (c != null && !c.validate(s, t, voisins))
                    return false; // ça bloque
            }
        }

        // sinon tout est bon à moins que la place soit déjà prise
        return true;
    }

    public String[] getAllInfo() {
        String[] infos = new String[donnees.getTables().length];
        int cpt = 0;

        for (int t : donnees.getTables()) {

            if (!donnees.isDeleted(t))
                infos[cpt] = donnees.getTableInfos(t);

            cpt++;
        }

        return infos;
    }

    // peut-être à supprimer
    public String getAllTable(int numTable) {
        return donnees.getTableInfos(numTable);
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

    public String descripData() {
        StringBuilder result = new StringBuilder();

        for (String s : donnees.descrip())
            result.append(s).append(";");

        return result.toString();
    }

    public String tabInfoForVisu(int nb) {
        return donnees.getInfosForVisu(nb);
    }
}