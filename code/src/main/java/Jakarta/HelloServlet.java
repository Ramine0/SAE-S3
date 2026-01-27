package Jakarta;


import org.NeoMalokVector.SAE_S3.Room;
import org.NeoMalokVector.SAE_S3.Table;
import placement.CreatingIntermediate;
import placement.GridMap;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {

        GridMap test ;
        test = new GridMap();
        Table[] tab = test.loadMap() ;
        int cpt = 0 ;
        System.out.println(tab);
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int[] adj : test.getMatriceAdj() ) {
            if (cpt != 0) {
                System.out.print(cpt+" ");
                for (int i : adj) System.out.print(i + " ");
                System.out.println();
            }
            cpt++ ;

        }


    }


}