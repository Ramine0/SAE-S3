package placement;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class CreatingIntermediate
{
    private Data d;

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

    public String afc()
    {
        return "as une donnée";
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
            return "etudiant non trouvé";
        }

    }

    public boolean findTable(int numTab)
    {
        return Utilitaire.in(numTab, d.freeTables());
    }

    public int findNumsForImp(String id, int num)
    {
        num = findTable(num) ? num : -1;
        if (id.equals("le num donné n'existe pas"))
        {
            return -1;
        } else if (id.length() > 8)
        {
            return 0;
        } else if (num == -1)
        {
            return -2;
        } else
        {
            return d.addImp(id, num);
        }
    }

    public void removeImp(int id)
    {
        d.removeConstraint("I", id - 1);
    }

    public int findStudentForGroup(String idPartiel, int numGrp)
    {
        String etu = findEtu(idPartiel);

        if (etu.equals("le num donné n'existe pas"))
        {
            return -1;
        } else if (etu.length() > 8)
        {
            return 0;
        } else
        {
            if (d.addStudentGroupConstraint(etu, numGrp))
            {
                return 1;
            } else
            {
                return 2;
            }
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
        num = findTable(num) ? num : -1;
        d.unremoveTable(num);
    }

    public boolean setDimensions(int lon, int lar)
    {
        return d.setDimensions(lon, lar);
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

}
