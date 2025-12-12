package utilitaire;

public class Utilitaire {

    public static boolean in(Object o, Object[] O){
        for (Object i : O){
            if (i==o) {
                return true;
            }
        }
        return false;
    }

    public static int pos(int nb, int[] tab) {

        for (int  i = 0; i < tab.length ; i++ ) {
            if (tab[i] == nb) {
                return i;
            }
        }

        return -1;
    }

    public static boolean in(int o, int[] O) {
        for (int i : O) {
            if (i == o) {
                return true;
            }
        }
        return false;
    }

}
