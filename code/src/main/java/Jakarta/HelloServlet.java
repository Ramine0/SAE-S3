package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet {

    static void main(String[] args) throws FileNotFoundException {
        Room salle = new Room();
        CreatingIntermediate crea = salle.getCrea();
        PositioningIntermediate pos;
        crea.setMode(1);
        pos = salle.getPositioningIntermediate();
        System.out.println(crea.findStudentForGroup("p2406410",1)) ;
        System.out.println(crea.getSeparated());
    }
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

//        for (int i = 0; i < crea.getNumberTables(); i++)
//        {
//            Table table = crea.getTable(i);
//
//            if (table == null)
//                continue;
//
//            System.out.println(table.getNum() + ". " + (table.getEtu() != null ? table.getEtu().getFirstName() : "aucun étu"));
//        }

}
