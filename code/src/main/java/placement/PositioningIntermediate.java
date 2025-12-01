package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;

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
        // sinon tout est ok
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