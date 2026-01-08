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
        System.out.println("m'ajouter : " + crea.findStudentForGroup("12406410", 1));
        System.out.println("me re ajouter : " + crea.findStudentForGroup("p2406", 1));
        System.out.println("remove une table : " + crea.supprTable(2));


    }


}