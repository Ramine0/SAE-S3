package placement;

import java.io.FileNotFoundException;

public class CreatingIntermediate {
    private Data d;
    public CreatingIntermediate(Data data) {
        d = data;
    }
    public void createTables(int lon, int lar){
        d.setNumberTables(lon*lar);
    }
    //public void createConstraint(String constraint){ // pas sur pour l'instant, faut voir ce que renvoie la view vis à vis des contraintes
    // Pas sur d'en avoir besoin pour le coup
    //}
}
