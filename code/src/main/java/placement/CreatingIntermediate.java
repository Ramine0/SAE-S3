package placement;

import constraints.Constraint;
import constraints.PerClass;
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
        d = new Data(path, "R11");
    }

    public void createTables(int lon, int lar)
    {
        d.setNumberTables(lon * lar);
    }

    public int getNumberTables()
    {
        return d.getTables().length;
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
        return Utilitaire.in(numTab, d.freeTables());
    }

    public Table getTable(int numTab)
    {
        return d.getTable(numTab);
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
            return -3;
        } else if (num == -1)
        {
            return -2;
        } else
        {
            return d.addImp(id, num);
        }
    }

    public void removeContrainst(String constr, int id)
    {
        d.removeConstraint(constr, id - 1);
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
            return 2;
        } else
        {
            if (d.isDeleted(num))
                return 2;

            else if (d.removeTable(num))
            {
                return 0;
            } else
            {
                return 1;
            }
        }
    }

    public void unremoveTable(int num)
    {
        d.unremoveTable(num);
    }

    public void setDimensions(int lon, int lar)
    {
        d.setDimensions(lon, lar);
    }

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

    public void setMode(int i)
    {
        if (i == 0)
            d.changeMode('N');
        else if (i == 1)
            d.getConstr()[0] = new PerClass(false);
        else if (i == 2)
            d.getConstr()[0] = new PerClass(true);
    }


    public PositioningIntermediate generatePos()
    {
        return new PositioningIntermediate(d);
    }

}
