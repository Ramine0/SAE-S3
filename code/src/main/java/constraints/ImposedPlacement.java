package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class ImposedPlacement extends Constraint
{
    private int numTable;
    private String numEtu;

    public ImposedPlacement(int numTable, String numEtu){
        this.numTable = numTable;
        this.numEtu = numEtu;
    }
    // pas besoin de valider on doit juste placer l'etu a la place qu'on lui a donné
    @Override
    public boolean validate(Student student, int table, Student[] etu) {
        return (student.getId().equals(numEtu) && table==numTable) ;
    }

    /**
     * Fonction permettant d'obtenir l'étudiant avec sa table. On devrait l'utiliser plus tard au début de la génération du placement
     * @return un tableau de string contenant le numéro de la table et le numéro de l'étudiant
     */
    public String[] getPaire() {
        String [] result =  {Integer.toString(numTable),numEtu} ;
        return result;
    }
    public int getNumTable() {
        return numTable;
    }
    public String getNumEtu() {
        return numEtu;
    }

}
