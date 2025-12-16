package placement;

import java.io.FileNotFoundException;

public class CreatingIntermediate {
    private Data d;
    public CreatingIntermediate() {
        try {
            d = new Data() ;
        }catch(Exception e) {}

    }
    public void createTables(int lon, int lar){
        d.setNumberTables(lon*lar);
    }

    public String afc() {
        return "as une donnée" ;
    }
    //public void createConstraint(String constraint){ // pas sur pour l'instant, faut voir ce que renvoie la view vis à vis des contraintes
    // Pas sur d'en avoir besoin pour le coup
    //}
}
