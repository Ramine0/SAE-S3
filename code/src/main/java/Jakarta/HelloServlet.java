package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet {

    static void main(String[] args) throws FileNotFoundException {
        Room salle = new Room();
        CreatingIntermediate crea = salle.getCreating();
        PositioningIntermediate pos = crea.generatePos();
        crea.setMode(0);

        crea.changeMapMode('R', "src/main/webapp/");

        crea.createTables(3, 3);
        crea.setDimensions(3, 3);

        System.out.println("add group constr Malik : "+  crea.findStudentForGroup("p2406", 1));
        System.out.println("add group constr Néo : " + crea.findStudentForGroup("p24033", 1));

        System.out.println(salle.generate());
    }

}
