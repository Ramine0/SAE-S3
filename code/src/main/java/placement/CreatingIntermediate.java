package placement;

import constraints.Constraint;
import constraints.PerGroup;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;


public class CreatingIntermediate
{
    private final Data d;

    public CreatingIntermediate() throws FileNotFoundException
    {
        d = new Data();
    }

    public CreatingIntermediate(String path) throws FileNotFoundException
    {
        d = new Data(path, "D");
    }

    public boolean createTables(int lon, int lar)
    {
        return d.setNumberTables(lon, lar);
    }

    public int getNumberTables()
    {
        return d.getTables().length;
    }

    public int minTable(){
        return d.minNumTable();
    }

    public int maxTable(){
        return d.maxNumTable();
    }

    public String findEtu(String id)
    {
        String trouve = d.completeId(id);
        if (!trouve.isEmpty())
        {
            return trouve;
        } else if (id.length() == 8)
        {
            return "le num donné n'existe pas";
        } else
        {
            return "1";
        }

    }

    public boolean findTable(int numTab)
    {
        return Utilitaire.in(numTab, free());
    }


    public int findNumsForImp(String id, int num)
    {
        id = findEtu(id);
        num = findTable(num) ? num : -1;

        if (id.equals("le num donné n'existe pas"))
        {
            return -1;
        } else if (id.length() > 8)
        {
            return -1;
        } else if (num == -1)
        {
            return -1;
        } else
        {
            return d.addImp(id, num);
        }
    }
    public void removeContrainst(String constr, int id)
    {
        d.removeConstraint(constr, id);
    }
    public Constraint[] getConstr(){
        return d.getConstr();
    }

    public String findStudentForGroup(String idPartiel, int numGrp)
    {
        String etu = findEtu(idPartiel);

        if (etu.length() == 8)
        {
            return d.addStudentGroupConstraint(etu, numGrp);
        } else
        {
            return etu;
        }
    }

    public String[] descripData()
    {
        return d.descrip();
    }

    public String studentInfo(String num)
    {
        Student student = d.getStudentFromId(num);
        if (student == null)
        {
            return null;
        }

        return student.getName() + " " + student.getFirstName();
    }

    public int supprTable(int num)
    {
        num = findTable(num) ? num : -1;

        if (num == -1)
        {
            return -2;
        } else
        {
            if (d.isDeleted(num))
                return -3;

            else if (d.isImposed(num)){
                return -4;
            }else {
                return d.removeTable(num) ;
            }
        }
    }

    public int[] free(){return d.freeTables();}

    public void unremoveTable(int num)
    {
        d.unremoveTable(num);
    }

    public void setDimensions(int lon, int lar) {d.setDimensions(lon, lar);}
    public Map getMap()
    {
        return d.getMap();
    }

    public String[] getImposedStud()
    {
        return d.imposedStudents();
    }

    public void resetData()
    {
        d.reset();
    }

    public Student StuFromTable(int num)
    {
        return d.getStuFromTab(num);
    }

    public int getNbConstr()
    {
        return d.getConstr().length;
    }

    public Constraint getConstr(int num)
    {
        return d.getConstr()[num - 1];
    }

    public Constraint getConstr(String type, int num){
        if (type.equals("I")){
            return d.getImposedPlacement(num);
        }else if (type.equals("G")){
            return d.getPerGroup(num);
        }else{
            return d.getConstr()[0];
        }
    }

    public void setMode(int i)
    {
        switch (i)
        {
            case 0 -> d.changeMode('N');
            case 1 -> d.changeMode('G');
            case 2 -> d.changeMode('S');
        }
    }


    public PositioningIntermediate generatePos()
    {
        return new PositioningIntermediate(d);
    }

    public boolean loadPlanDefault(String path) {
        return d.loadPlanDefault(path);
    }

    public boolean changePlanMode(char newOne, String path) {
        return d.changePlanMode(newOne, path);
    }

    public String tableValidateButton(int oldNum, int newNum, String numEtu) {
        String result = "";
        if (oldNum != 0 && tableExist(oldNum) && newNum > 0 && ! tableExist(newNum)) {
            if ( d.changeNumTable(oldNum, newNum) ) {
                result += newNum + ";";
            }else {
                result += "error;";
            }
        }else if (oldNum != 0 ){
            result+= "invalid;" ;
        }

        if (numEtu != ""  && findTable(newNum)){
            result += findNumsForImp(numEtu,newNum);
        }else {
            result += "";
        }

        return result ;
    }

    public boolean tableExist(int numTab) {
        return d.tableExist(numTab) ;
    }

    public String getSeparated() {
        String result = "";
        for (int i = 1; i < 10 ; i++ ) {
            PerGroup temp = d.getPerGroup(i) ;
            if (temp == null) {
                break ;
            }else {
                String[] students = temp.toString().split(";") ;
                for (String s : students) {
                    if (! s.isEmpty()) {
                        String id = findEtu(s) ;
                        result += id+":"+d.getFullName(id)+";" ;
                    }
                }
                result += "!" ;
            }
        }

        return result ;
    }

    public String getStudentList() {
        String result = "ID             ; nom prenom \n";
        result += d.studentList() ;
        return result ;
    }
}
