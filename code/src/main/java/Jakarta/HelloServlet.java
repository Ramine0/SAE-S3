package Jakarta;


import constraints.ImposedPlacement;
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
        crea.createTables(10, 4);
        crea.setDimensions(10, 4);
        PositioningIntermediate pos;
        pos = crea.generatePos();;

        crea.setMode(1);
        pos.creerPlacement();
        System.out.println(pos.getTablesForVisu());
    }


}