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
        crea.createTables(10, 4);
        crea.setDimensions(10, 4);
        PositioningIntermediate pos;
        pos = salle.getPositioningIntermediate();

        crea.setMode(0);
        crea.supprTable(1);
        crea.supprTable(2);
        crea.supprTable(40);
        crea.supprTable(39);
        for (int i=0; i<crea.del().length; i++){
            System.out.println(crea.del()[i]);
        }
        System.out.println("__________________________");
        crea.unremoveTable(40);
        crea.unremoveTable(39);
        for (int i=0; i<crea.del().length; i++){
            System.out.println(crea.del()[i]);
        }
        System.out.println("__________________________");
        crea.supprTable(40);
        crea.supprTable(39);
        for(int i=0; i<crea.del().length; i++){
            System.out.println(crea.del()[i]);
        }



//        crea.findNumsForImp("p24033", 1);
//        crea.findNumsForImp("p24039", 2);
//        crea.findNumsForImp("p2406", 3);
//        crea.findNumsForImp("p12", 4);
    }


}