package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.Random;

public class PositioningIntermediate {
    private final Data donnees; // contient les étudiants et les tables
    // On ne manipule pas directement les tables, on a juste leur numéro. Question d'optimisation et de securité

    private final Random random = new Random();

    // on a besoin des données (les vraies pas une copie)
    public PositioningIntermediate(Data d) {
        donnees = d;
    }

    // la fonction principale du positionning qui genere le placement
    public boolean creerPlacement() {
        donnees.placerImposes(); // on commence par les etudiants imposés a des places

        // un compteur de tentatives de placement sur une table
        int loopCount;
        int tableNumber = 0;

        // tant qu'on a des etudiants non placés et qu'on a pas tester toute les tables
        while (donnees.freeStudents().length != 0 && tableNumber <= donnees.maxTableID()) {
            tableNumber++;// on teste la table suivante

            // on verifie qu'elle existe
            if (!Utilitaire.in(tableNumber, donnees.freeTables()))
                continue;

            String[] freeStudents = donnees.freeStudents(); // on recupere les etudiants a placer
            String studentId = freeStudents[random.nextInt(freeStudents.length)]; // on en prend un au hazard

            loopCount = 0; // on commence a compter

            // tant que l'etu selectionné est pas placable sur la table actuelle
            while (!walid(donnees.getStudentFromId(studentId), tableNumber)) {
                // on teste un autre etu
                studentId = freeStudents[random.nextInt(freeStudents.length)];
                // on compte les try
                loopCount++;

                if (loopCount > freeStudents.length / 2) // si on a tester la moitié des etus
                    tableNumber++; // on passe a la table suivante
            }

            // vu que c ok alors on place l'etu
            donnees.placeStudent(tableNumber, studentId);
                // je sais pas quoi y mettre !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }

        // si ya encore des etus c'est que ca chargé en contraintes
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

    // renvoie une grosse string degeu qui donne les infos des tables
    public String getTablesForVisu() {
        // on commence par la taille du plan
        StringBuilder result = new StringBuilder(donnees.getPlanSize() + "/");

        // on cherche les tables pas supprimées et on donne leurs infos
        for (int t : donnees.existingTables())
            if (!donnees.isDeleted(t))
                result.append(donnees.getTableInfos(t)).append(";");

        return result.toString();
    }

    // ca echange les etus des tables 1 et 2
    public boolean swapPlaces(int numT1, int numT2) {
        if (Utilitaire.in(numT1, donnees.existingTables()) && Utilitaire.in(numT2, donnees.existingTables()))
            return donnees.swap(numT1, numT2);

        return false;
    }

    // une fonction de debug qui permet d'avoir un ptit rendu du placement
    public String descripData() {
        StringBuilder result = new StringBuilder();

        for (String s : donnees.descrip())
            result.append(s).append(";");

        return result.toString();
    }

    // qqc cloche mais jsp quoi a revoir
    public String tabInfoForVisu(int nb) {
        return donnees.getInfosForVisu(nb);
    }
}