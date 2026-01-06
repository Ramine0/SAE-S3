package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {
        Room salle = new Room();

        salle.getCrea().createTables(1, 1);

        salle.getCrea().supprTable(1);

        salle.getCrea().descripData();

        salle.getCrea().unremoveTable(1);

        System.out.println();

        salle.getCrea().descripData();
    }


}