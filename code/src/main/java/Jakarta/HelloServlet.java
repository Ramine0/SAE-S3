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
        Room salle = new Room() ;
        CreatingIntermediate crea = salle.getCrea() ;
        crea.createTables(2, 2);
        crea.setDimensions(2, 2);
        PositioningIntermediate pos ;
        pos = salle.getPositioningIntermediate() ;
        System.out.println("creer un grp : "+ crea.findStudentForGroup("12406410",1)) ;
        System.out.println("creer un grp : "+ crea.findStudentForGroup("12y06410",1));


    }


}