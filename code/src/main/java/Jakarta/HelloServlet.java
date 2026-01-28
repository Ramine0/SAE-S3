package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import org.NeoMalokVector.SAE_S3.Table;
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
        salle.positioningMode();

        System.out.println("Génération réussi : " + salle.generate());
        System.out.println();

        for (int i = 0; i < crea.getNumberTables(); i++)
        {
            Table table = crea.getTable(i);

            if (table == null)
                continue;

            System.out.println(table.getNum() + ". " + (table.getEtu() != null ? table.getEtu().getFirstName() : "aucun étu"));
        }

    }


}