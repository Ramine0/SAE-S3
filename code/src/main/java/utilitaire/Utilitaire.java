package utilitaire;

public class Utilitaire {


    public static int pos(int nb, int[] tab) {

        for (int  i = 0; i < tab.length ; i++ ) {
            if (tab[i] == nb) {
                return i;
            }
        }

        return -1;
    }
}
