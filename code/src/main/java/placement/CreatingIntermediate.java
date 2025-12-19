package placement;

import utilitaire.Utilitaire;

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

    public String findEtu(String id) {
        String trouve = d.completeId(id) ;
        if (trouve != "") {
            return trouve;
        }else if (id.length()==8) {
            return "le num donné n'existe pas" ;
        }else {
            return "etudiant non trouvé" ;
        }

    }

    public boolean findTable(int numTab) {
        return Utilitaire.in(numTab,d.freeTables()) ;
    }


    public int findStudentForGroup (String idPartiel,int numGrp) {
        String etu = findEtu(idPartiel) ;
        if (etu.equals("le num donné n'existe pas")) {
            return -1 ;
        }else if (etu.length() > 8) {
            return 0 ;
        }else {
            if (d.addStudentGroupConstraint(etu,numGrp)) {
                return 1;
            }else {
                return 2 ;
            }
        }

    }

    public boolean addGrp() {
        return d.addGrp() ;
    }


    //public void createConstraint(String constraint){ // pas sur pour l'instant, faut voir ce que renvoie la view vis à vis des contraintes
    // Pas sur d'en avoir besoin pour le coup
    //}
}
