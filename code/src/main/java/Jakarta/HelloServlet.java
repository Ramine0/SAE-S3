package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {
        Room salle = new Room() ;
        System.out.println(salle.crea.descripData());
        System.out.println(salle.crea.findEtu("p2406")) ;
        System.out.println(salle.crea.findNumsForImp("p2406",1)) ;



    }


}