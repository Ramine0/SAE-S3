package Jakarta;


import placement.Data;
import placement.PositioningIntermediate;

import java.sql.SQLOutput;

public class HelloServlet
{

    static void main(String[] args)
    {
        System.out.println("Hello World");

        Data d =  new Data();


        PositioningIntermediate intermediate = new PositioningIntermediate("R00", null, d);
        intermediate.CreerPlacement();
        System.out.println(d.descrip());
    }
}