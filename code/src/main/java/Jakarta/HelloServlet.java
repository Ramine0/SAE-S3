package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet {

    static void main(String[] args) throws FileNotFoundException {
        Room room = new Room();
        CreatingIntermediate crea = room.getCreating();
        crea.setMode(0);

        crea.changeMapMode('D', "src/main/webapp/");

      crea.separeStudentsPerGroup("p2406", 1);
      crea.separeStudentsPerGroup("p24033", 1);

        room.generate();


    }

}
