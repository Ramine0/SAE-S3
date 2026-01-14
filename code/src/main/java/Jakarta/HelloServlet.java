package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {
        Room salle = new Room();
        CreatingIntermediate crea = salle.getCrea();
        crea.createTables(3, 3);
        crea.setDimensions(3, 3);
        PositioningIntermediate pos;
        pos = salle.getPositioningIntermediate();

        crea.setMode(0);

        crea.findStudentForGroup("p2406", 1);
        crea.findStudentForGroup("p24033", 1);
        crea.findStudentForGroup("p24039", 1);

        crea.generatePos().creerPlacement();

        for (int i = 0; i < crea.getNumberTables(); i++)
        {
            if (crea.getTable(i) != null)
                if (crea.getTable(i).getEtu() != null)
                    System.out.println(crea.getTable(i).getNum() + " " + crea.getTable(i).getEtu().getFirstName());
                else
                    System.out.println(crea.getTable(i).getNum() + " null");
        }
    }


}