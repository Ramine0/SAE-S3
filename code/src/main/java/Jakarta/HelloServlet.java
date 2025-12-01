package Jakarta;


import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileReader;
import java.sql.SQLOutput;

public class HelloServlet
{

    static void main(String[] args)
    {

        Data d = null ;
        try {
            d = new Data();
        } catch (Exception e) {
            System.out.println("erreur etudiants non trouvés");
        }
        PositioningIntermediate intermediate = new PositioningIntermediate("R00", null, d);
        intermediate.CreerPlacement();




    }
}