package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;

public class ImposedPlacement extends Constraint
{
    public int numTable;
    public String numEtu;

    public ImposedPlacement(int numTable, String numEtu){
        this.numTable = numTable;
        this.numEtu = numEtu;
    }
    // pas besoin de valider on doit juste placer l'etu a la place qu'on lui a donné
    @Override
    public boolean validate(Student student, Table table, Student[] etu) {
        return (student.getId().equals(numEtu) && table.getNum()==numTable) ;
    }

    public String[] getPaire() {
        String [] result =  {Integer.toString(numTable),numEtu} ;
        return result;
    }

}
