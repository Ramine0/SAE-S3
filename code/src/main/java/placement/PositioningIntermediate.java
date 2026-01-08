package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public class PositioningIntermediate
{


    // on passe par donnees pour acceder au données (etus et tables)
    // on manipule pas directement les tables on a juste leur numeros question d'optimisation et de securité
    private Data donnees;
    // on fait ce qu'on veux des contraintes c plus simple et + pratique

    // Ici constructeur de l'intermediaire il prends en paramettre une sting qui donne les infos du format de plan
    // charAt(0) c le type (rectangle) et les 2 suivants c l et L (pour rect)
    // on donne aussi les numero de tables supprimées
    // on donne pas le fichier d'etu car comme il y en a qu'1 on saura deja comment et ou on va l'enregistrer
    // on va lme lire ici MAIS il faudra pour ca le save qqp AVANT

    public PositioningIntermediate(Data d)
    {
        donnees = d;
    }

    private PositioningIntermediate(String path)
    {
        try
        {
            donnees = new Data(path, "R");
        } catch (Exception e) {System.out.println(e.getMessage());}

    }

    public boolean creerPlacement()
    {
        donnees.placerImposes();

        int tableNumber = 1;

        for (String studentId : donnees.freeStudents())
        {
            while (!Utilitaire.in(tableNumber, donnees.freeTables()))
            {
                tableNumber++;

                if (tableNumber > donnees.existingTables().length)
                    break;
            }

            if (walid(donnees.getStudentFromId(studentId), tableNumber))
            {
                donnees.placeStudent(tableNumber, studentId);
            }

            tableNumber++;
        }

        // le reste du la fonction (placer les etu aleatoirement en tenant compte du validateç
        /*
        faire une boucle qui parcours les etus et les places petit a petit sur les places aleatiores si walid
        Ne pas oublier que si on a q'1 etu et que c pas walid on doit echanger aleatoirement avec etu donc la place est
         */

        /*
         * Bon dcp on va faire autrement pour insérer l'aléatoire plus facilement:
         * Grosso modo on parcours les tables dans l'ordre croissant jusque soit qu'il y en ait plus, soit qu'il y ait
         * plus d'etu a placer. Pour l'aléatoire, on prend un étu aléatoire parmi les étudiants pas placés. Si la table
         * est pas dans les places libres, on passe à la suivante, sinon on essaye de placer l'étudiant, en prenant
         * compte des contraintes, et si on peut le placer on passe à la table suivante.
         * */
        //on commence à la table 1

        return donnees.freeStudents().length == 0;
    }

    // valide ou non le placement
    private boolean walid(Student s, int t)
    {
        if (!Utilitaire.in(t, donnees.freeTables()))
            return false;

        // si on sait que l'etu as des contraintes
        if (Constraint.contraint(s.getId()))
        {

            // on prends les tables voisines pour regarder
            Student[] voisins = donnees.neighbours(t);
            for (Constraint c : donnees.getConstr())
            {
                // si ca bloque
                if (!c.validate(s, t, voisins))
                {
                    return false; // ca bloque
                }
            }

        }
        // sinon tout est ok à moins que la place soit déjà prise
        return true;
    }

    public String[] getAllInfo()
    {
        String[] infos = new String[donnees.getTables().length];
        int cpt = 0;
        for (int t : donnees.getTables())
        {

            if (!donnees.isDeleted(t))
                infos[cpt] = donnees.getTableInfos(t);

            cpt++;
        }
        return infos;
    }

    public String getAllTable(int numTable)
    {
        return donnees.getTableInfos(numTable);
    }

    public String getTabInfoForVisu() {
        String result ="" ;
        for (String s : getAllInfo()){
            result = result.concat( s +":");
        }
        return result ;
    }

    public String getTablesForVisu() {
        String result ="" ;
        for (int t : donnees.getTables()) {
            if (donnees.haveStudent(t)) {
                result = result.concat(t+";") ;
            }
        }

        return result ;
    }

    public boolean swapPlaces(int numT1, int numT2) {
        if (Utilitaire.in(numT1, donnees.existingTables()) && Utilitaire.in(numT2,donnees.existingTables())) {
            return donnees.swap(numT1,numT2);
        }
        return false ;
    }

}