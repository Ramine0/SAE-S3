package Jakarta;


import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileReader;
import java.sql.SQLOutput;

public class HelloServlet
{

    static void main(String[] args)
    {

        Data d=null ;
        try {


            PositioningIntermediate intermediate = new PositioningIntermediate("R00", null, d);
            intermediate.CreerPlacement();
            for (String s :d.descrip() ) {
                System.out.println(s);
            }
            new FileReader("src/main/webapp/resources/etudiants.csv") ;


        } catch (Exception e) {
            System.out.println("erreur etudiants non trouvés");
        }




    }
}