package Jakarta;


import placement.PositioningIntermediate;

public class HelloServlet
{

    static void main(String[] args)
    {
        System.out.println("Hello World");

        PositioningIntermediate intermediate = new PositioningIntermediate("R00", null, null);
        intermediate.CreerPlacement();
    }
}