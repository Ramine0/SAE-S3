package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import org.NeoMalokVector.SAE_S3.Student;
import placement.CreatingIntermediate;
import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {
        Room salle = new Room();
        System.out.println(salle.getCrea().changePlanMode('D'));
        salle.getCrea().setMode(0);

    }


}