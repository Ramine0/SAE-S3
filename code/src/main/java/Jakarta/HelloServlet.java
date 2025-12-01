package Jakarta;


import placement.Data;
import placement.PositioningIntermediate;

import java.io.FileNotFoundException;

public class HelloServlet
{

    static void main(String[] args) throws FileNotFoundException
    {



            Data d = new Data();

            PositioningIntermediate intermediate = new PositioningIntermediate("R00", null, d);
            intermediate.CreerPlacement();

            for (String s : d.descrip())
            {
                System.out.println(s);
            }
//            new FileReader("../webapp/resources/etudiants.csv");

    }
}