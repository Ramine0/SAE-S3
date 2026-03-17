package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet {

    static void main(String[] args) throws FileNotFoundException {
        Room salle = new Room();
        CreatingIntermediate crea = salle.getCrea();
        crea.setMode(1);
        crea.loadPlanDefault("src/main/webapp/");
        salle.generate() ;
        System.out.println(salle.getPositioningIntermediate().getTablesInfoForVisu()) ;
    }

}
