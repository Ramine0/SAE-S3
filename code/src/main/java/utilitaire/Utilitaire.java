package utilitaire;

public class Utilitaire
{


    public static int pos(int nb, int[] tab)
    {

        for (int i = 0; i < tab.length; i++)
        {
            if (tab[i] == nb)
            {
                return i;
            }
        }

        return -1;
    }

    public static String printName(String name)
    {
        return "Salut " + name + " Grand et Talentueux et Intelligent";
    }

    public static void onHover()
    {
        System.out.println("onHover()");
    }
}
