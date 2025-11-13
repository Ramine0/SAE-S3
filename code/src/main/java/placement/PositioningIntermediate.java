package placement;

import constraints.Constraint;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class PositioningIntermediate
{
    public Map map;

    public Table[] deletedTables;
    public Constraint[] constraints;
    public Table[] tables;
    public Student[] students;

    public boolean validatePlacement(Table table, int studentNumber)
    {
        return false;
    }

    public void imposePlaces(Table table, Student student)
    {
        table.student = student;
    }

    // Ici constructeur de l'intermediaire il prends en paramettre une sting qui donne les infos du format de plan
    // charAt(0) c le type (rectangle) et les 2 suivants c l et L (pour rect)
    // on donne aussi les numero de tables supprimées
    // on donne pas le fichier d'etu car comme il y en a qu'1 on saura deja comment et ou on va l'enregistrer
    // on va lme lire ici MAIS il faudra pour ca le save qqp AVANT
    public PositioningIntermediate (String mapType, int[] deleted) {

        if (mapType.charAt(0) == 'R') {
            // plan rectangulaire
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)),Character.getNumericValue(mapType.charAt(2))) ;
        }

    }
}
