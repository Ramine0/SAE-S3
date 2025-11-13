package utilitaire;



public class Utilitaire {

    public static boolean in(Object item, Object[] tab) {

        for (int  i = 0; i < tab.length ; i++ ) {
            if (tab[i] == item) {
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


}
