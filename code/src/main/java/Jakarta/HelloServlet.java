package Jakarta;


import placement.CreatingIntermediate;
import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {
        CreatingIntermediate crea = new CreatingIntermediate() ;
        System.out.println(crea.addGrp()) ;
        System.out.println(crea.findStudentForGroup("p2406",0)) ;
        System.out.println(crea.findStudentForGroup("p24039",0)) ;
        System.out.println(crea.findStudentForGroup("p2403",0)) ;
        System.out.println(crea.findStudentForGroup("p24039",0)) ;





    }


}