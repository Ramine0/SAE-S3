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
    }


}