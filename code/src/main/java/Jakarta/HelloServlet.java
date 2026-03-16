package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;
import java.util.Arrays;

import org.NeoMalokVector.SAE_S3.Table;

public class HelloServlet {

    static void main(String[] args) throws FileNotFoundException {
        Room salle = new Room();
        CreatingIntermediate crea = salle.getCrea();
        PositioningIntermediate pos;
        crea.setMode(0);
        crea.loadPlanDefault("src/main/webapp/");

        pos = salle.getPositioningIntermediate();
        crea.findStudentForGroup("p2406410", 1);
        crea.findStudentForGroup("p24033", 1);
        System.out.println(crea.getSeparated());

        System.out.println(salle.generate());
//        salle.positioningMode();
//
//        System.out.println("Génération réussi : " + salle.generate());
//        System.out.println();
//
//        String studentId = crea.findEtu("p24033");
//
//        String result = studentId + ";";
//        result += crea.studentInfo(studentId) + ";";


//
//        String tableNumber = "1";
//
//        if (Integer.parseInt(tableNumber) <= 0 || Integer.parseInt(tableNumber) > crea.maxTable())
//            result += "3;";
//        else if (tableNumber.isEmpty())
//            result += "null;";
//        else
//            result += crea.findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";
//
//        System.out.println(result);

    }
}