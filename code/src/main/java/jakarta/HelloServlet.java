package jakarta;


import org.the_disabled.sae_s3.Room;
import placement.CreatingIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet {

    static void main(String[] args) throws FileNotFoundException {
        Room room = new Room();
        CreatingIntermediate crea = room.getCreating();
        crea.setMode(1);

        //        crea.changeMapMode('D', "src/main/webapp/");

        room.generate();

        System.out.println(room.getPositioning().getTablesForVisu());

    }

}
