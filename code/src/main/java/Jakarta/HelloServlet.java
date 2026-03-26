package Jakarta;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.Data;
import placement.PositioningIntermediate;

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
