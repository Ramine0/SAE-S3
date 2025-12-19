package placement;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

public class CreatingIntermediate {
    private Data d;
    public CreatingIntermediate() {
        try {
            d = new Data() ;
        }catch(Exception e) {}

    }
    public CreatingIntermediate(String path) {
        try {
            d = new Data(path) ;
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

    public int findNumsForImp (String id, int num) {
        String etu = findEtu(id) ;
        num = findTable(num) ? num : -1 ;
        if (etu.equals("le num donné n'existe pas")) {
            return -1 ;
        }else if (etu.length() > 8) {
            return 0 ;
        }else if (num == -1 ) {
            return -2 ;
        }else {
            if (d.addImp(etu,num)) {
                return 1;
            }else {
                return 2 ;
            }
        }
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

    public String[] descripData() {
        return d.descrip() ;
    }

    public String studentInfo (String num) { Student student = d.getStudentFromId(num); return student.getName() + " " + student.getFirstName() ;}

    public int supprTable(int num) {
        num = findTable(num) ? num : -1 ;
        if (num == -1 ) {
            return -2 ;
        }else {
            if (d.removeTable(num) && ! d.isDeleted(num)) {
                return 1;
            }else {
                return 2 ;
            }
        }
    }

    public Data getData()
    {
        return d;
    }

    //public void createConstraint(String constraint){ // pas sur pour l'instant, faut voir ce que renvoie la view vis à vis des contraintes
    // Pas sur d'en avoir besoin pour le coup
    //}
}
