package placement;


import constraints.Constraint;
import constraints.ImposedPlacement;
import constraints.PerClass;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

// la classe va save les données pour les creer via un CreatingIntermediate et les avoir dans le positioning intermediate
// elle aura plein de fonction utiles comme le lecture du fichier des etus ou le traitement des etus
public class Data {

    private Constraint[] constraints;
    private Table[] tables;
    private Student[] students;







    public Constraint[] getConstr() {return constraints;}
    public Table[] getTables() {return tables;}
    public Student[] getEtus() {return students;}


}
