package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.util.ArrayList;

public class PositioningIntermediate
{
    private Map map;

    // on passe par donnees pour acceder au données (etus et tables)
    // on manipule pas directement les tables on a juste leur numeros question d'optimisation et de securité
    private Data donnees;
    // on fait ce qu'on veux des contraintes c plus simple et + pratique


    // Ici constructeur de l'intermediaire il prends en paramettre une sting qui donne les infos du format de plan
    // charAt(0) c le type (rectangle) et les 2 suivants c l et L (pour rect)
    // on donne aussi les numero de tables supprimées
    // on donne pas le fichier d'etu car comme il y en a qu'1 on saura deja comment et ou on va l'enregistrer
    // on va lme lire ici MAIS il faudra pour ca le save qqp AVANT

    public PositioningIntermediate(String mapType, int[] deleted, Data d)
    {

        donnees = d;
        if (mapType.charAt(0) == 'R')
        {
            // plan rectangulaire
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)), Character.getNumericValue(mapType.charAt(2)));
        }

    }


    public void CreerPlacement()
    {
        donnees.placerImposes();
        // le reste du la fonction (placer les etu aleatoirement en tenant compte du validate
        /*
        faire une boucle qui parcours les etus et les places petit a petit sur les places aleatiores si walid
        Ne pas oublier que si on a q'1 etu et que c pas walid on doit echanger aleatoirement avec etu donc la place est

         */
/*
        for (Student student : donnees.getEtus())
        {
            for (int table : donnees.getTables())
                if (walid(student, table))
                    donnees.placeStudent(table, student.getId());
        }
*/
        /*
        * Bon dcp on va faire autrement pour insérer l'aléatoire plus facilement:
        * Grosso modo on parcours les tables dans l'ordre croissant jusque soit qu'il y en ait plus, soit qu'il y ait
        * plus d'etu a placer. Pour l'aléatoire, on prend un étu aléatoire parmi les étudiants pas placés. Si la table
        * est pas dans les places libres, on passe à la suivante, sinon on essaye de placer l'étudiant, en prenant
        * compte des contraintes, et si on peut le placer on passe à la table suivante.
        * */
        //on commence à la table 1
        int table=1;
        //on parcours les tables jusqu'à la dernière ou jusqu'à ce qu'il n'y ait plus d'étu à placer
        while (table<donnees.getTables().length || donnees.freeStudents().length==0){
            int idStudent=(int)(Math.random()*donnees.freeStudents().length); // etu aléatoire parmis les non placés
            if (Utilitaire.in(table, donnees.freeTables())) {// on verifie que la table soit dans les places libres
                if (walid(donnees.getStudentFromId(donnees.freeStudents()[idStudent]), table)) {// on vérifie qu'on puisse placer l'etu
                    donnees.placeStudent(table, donnees.freeStudents()[idStudent]); // on place l'etu à table
                    table++; // on passe à la table suivante
                }
            }else{// s'il la table n'est pas dans les places libres (retirée ou déjà prise):
                table++;// on passe à la table suivante
            }
        }
    }

    // valide ou non le placement
    private boolean walid(Student s, int t)
    {
        // si on sait que l'etu as des contraintes
        if (Constraint.contraint(s))
        {

            // on prends les tables voisines pour regarder
            Student[] voisins = neighbours(t);
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

    // je prends les voisins de ma table
    private Student[] neighbours(int t)
    {
        ArrayList<Student> voisins = new ArrayList<>();
        // pour tous les voisins de la map
        for (int i : map.neighbours(t, donnees.freeTables()))
        {

            //je recupere l'etu de la table si on a bien une table
            if (i != -1)
            {
                voisins.add(donnees.getStuFromTab(i));
            }
        }

        return voisins.toArray(new Student[0]);
    }


}