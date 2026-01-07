package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;

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
        System.out.println("imposer la place a moi : " + crea.findNumsForImp("12406410", 1));
        System.out.println("imposer la place a Néo : " + crea.findNumsForImp("p2403367", 3));

        System.out.println("remove une table : " + crea.supprTable(2));
        System.out.println("changer le mode : " + salle.positioningMode());
        crea = null;
        pos = salle.getPositioningIntermediate();
        System.out.println("lancer la generation : " + salle.generate());
        for (String s : pos.getAllInfo())
        {
            System.out.println(s);
        }

    }


}