package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args)
    {
        Room salle = new Room() ;
        System.out.println(salle.getCrea().descripData());
        System.out.println(salle.getCrea().findEtu("p2406")) ;
        System.out.println(salle.getCrea().findNumsForImp("p2406",1)) ;



    }


}