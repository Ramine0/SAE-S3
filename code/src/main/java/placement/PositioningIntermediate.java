package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.Random;

public class PositioningIntermediate {


    // on passe par donnees pour acceder au données (etus et tables)
    // on manipule pas directement les tables on a juste leur numeros question d'optimisation et de securité
    private Data donnees;
    private final Random random = new Random();

    // on fait ce qu'on veux des contraintes c plus simple et + pratique

    // Ici constructeur de l'intermediaire il prends en paramettre une sting qui donne les infos du format de plan
    // charAt(0) c le type (rectangle) et les 2 suivants c l et L (pour rect)
    // on donne aussi les numero de tables supprimées
    // on donne pas le fichier d'etu car comme il y en a qu'1 on saura deja comment et ou on va l'enregistrer
    // on va lme lire ici MAIS il faudra pour ca le save qqp AVANT

    public PositioningIntermediate(Data d) {
        donnees = d;
    }

    private PositioningIntermediate(String path) {
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

        // j'ai trop la flemme de lire tout ce que Malik a écrit parce que c'est d'la merde et je sais même pas si c'est vraiment utile
        // comme si Malik était utile
        // lol
        // cet arabe là
        // psychopathe en plus
        // mais ça c'est une bonne chose
        // surtout pour faire chier Vector
        // il a rien fait le pauvre


        // le reste du la fonction (placer les etu aleatoirement en tenant compte du validateç
        /*
        faire une boucle qui parcours les etus et les places petit a petit sur les places aleatiores si walid
        Ne pas oublier que si on a q'1 etu et que c pas walid on doit echanger aleatoirement avec etu donc la place est
         */


        return donnees.freeStudents().length == 0;
    }

    // valide ou non le placement
    private boolean walid(Student s, int t) {
        System.out.println(s.getFullName());

        if (!Utilitaire.in(t, donnees.freeTables()))
            return false;

        // si on sait que l'etu as des contraintes
        if (Constraint.contraint(s.getId()) || donnees.hasMode()) {
            // on prends les tables voisines pour regarder
            Student[] voisins = donnees.neighbours(t);

            for (Constraint c : donnees.getConstr()) {
                // si ca bloque
                if (c != null) {

                    System.out.println(s.getFullName() + " c is not null");

                    if (!c.validate(s, t, voisins)) {
                        System.out.println(s.getFirstName() + " " + "not validating neighbours");
                        return false; // ca bloque
                    }
                }
            }
        }
        // sinon tout est ok à moins que la place soit déjà prise

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

    public String getAllTable(int numTable) {
        return donnees.getTableInfos(numTable);
    }

    public String getTablesInfoForVisu() {
        String result = "";
        for (String s : getAllInfo()) {
            result = result.concat(s + ":");
        }
        return result;
    }

    public String getTablesForVisu() {
        StringBuilder result = new StringBuilder(donnees.getPlanSize() + "/");
        for (int t : donnees.existingTables()) {
            if (!donnees.isDeleted(t)) {
                result.append(donnees.getTableInfos(t)).append(";");
            }
        }
        return result.toString();
    }

    public boolean swapPlaces(int numT1, int numT2) {
        if (Utilitaire.in(numT1, donnees.existingTables()) && Utilitaire.in(numT2, donnees.existingTables())) {
            return donnees.swap(numT1, numT2);
        }
        return false;
    }

    public String descripData() {
        StringBuilder result = new StringBuilder();
        for (String s : donnees.descrip()) {
            result.append(s).append(";");
        }
        return result.toString();
    }

    public String tabInfoForVisu(int nb) {
        return donnees.getInfosForVisu(nb);
    }
}