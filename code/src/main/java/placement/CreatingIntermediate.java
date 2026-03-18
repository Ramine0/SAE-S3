package placement;

import constraints.PerGroup;
import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;

/**
 * Classe utilisée comme contrôleur de la création des données (contraintes, étudiants...)
 */
public class CreatingIntermediate {
    private final Data d;
    public String msg ;

    /**
     * Constructeur par défaut de la classe.
     * @throws FileNotFoundException envoyée par Data() si elle ne trouve pas de fichier
     */
    public CreatingIntermediate() throws FileNotFoundException {
        d = new Data();
    }

    /**
     * Constructeur de la classe. À quoi il sert, bonne question???
     * @param path le chemin vers le fichier
     * @throws FileNotFoundException envoyée par Data() si elle ne trouve pas le fichier
     */
    public CreatingIntermediate(String path) throws FileNotFoundException {
        d = new Data(path, "D");
    }

    /**
     * Contrôle de création d'un plan rectangulaire
     * @param lon nombre de tables en longueur
     * @param lar nombre de tables en largeur
     */
    public void createTables(int lon, int lar)
    {
        d.setNumberTables(lon, lar);
    }

    /**
     * Fonction de récupération du nombre de tables
     * @return le nombre de tables de la salle
     */
    public int getNumberTables()
    {
        return d.getTables().length;
    }

    /**
     * Fonction de récupération du numéro de table minimum
     * @return le numéro de table le plus faible
     */
    public int minTable(){
        return d.minNumTable();
    }

    /**
     * Fonction de récupération du numéro de table maximum
     * @return le numéro de table le plus important
     */
    public int maxTable(){
        return d.maxNumTable();
    }

    /**
     * Fonction de recherche d'un étudiant à partir de l'id
     * @param id numéro étudiant, complet ou non
     * @return l'id complété ou un message adapté
     */
    public String findStudent(String id)
    {
        String trouve = d.completeId(id);
        if (!trouve.isEmpty()) {
            return trouve;
        } else if (id.length() == 8) {
            return "le num donné n'existe pas";
        } else {
            return "1";
        }

    }

    /**
     * Vérifie que la table numTab soit dans les tables libres
     * @param numTab le numéro de la table
     * @return true si elle fait partie des tables libres, false sinon
     */
    public boolean findTable(int numTab) {
        return Utilitaire.in(numTab, free());
    }


    /**
     * Regarde si les informations id et num sont correctement renseignées, si c'est le cas appelle
     * la logique d'ajout de place imposée de Data
     * @param id numéro de l'étudiant
     * @param num numéro de la table
     * @return un entier correspondant à la situation
     */
    public int findNumsForImp(String id, int num) {
        id = findStudent(id);
        num = findTable(num) ? num : -1;

        if (id.equals("le num donné n'existe pas")) {
            return -1;
        } else if (id.length() > 8) {
            return -1;
        } else if (num == -1) {
            return -1;
        } else {
            return d.addImp(id, num);
        }
    }

    /**
     * Retire la contrainte de type constr et d'index id
     * @param constr type de la contrainte
     * @param id index de la contrainte
     */
    public void removeConstraint(String constr, int id) {
        d.removeConstraint(constr, id);
    }

    /**
     * Vérifie que les informations soient correctement renseignées, si c'est le cas, entre dans
     * la logique d'ajout d'étudiant au groupe numGrp
     * @param idPartiel numéro de l'étudiant, complet ou non
     * @param numGrp groupe auquel on veut ajouter l'étudiant
     * @return en cas de succès, le message de la logique dans Data, sinon l'id partiel
     */
    public String findStudentForGroup(String idPartiel, int numGrp) {
        String student = findStudent(idPartiel);

        if (student.length() == 8) {
            return d.addStudentGroupConstraint(student, numGrp);
        } else {
            return student;
        }
    }

    /**
     * Fonction utilisée pour afficher la description des données
     * @return la description des données
     */
    public String[] dataDescription() {
        return d.descrip();
    }

    /**
     * Fonction de récupération de l'étudiant de numéro étudiant num
     * @param num numéro de l'étudiant
     * @return null si l'étudiant n'est pas trouvé, son nom et son prénom sinon
     */
    public String studentInfo(String num) {
        Student student = d.getStudentFromId(num);
        if (student == null) {
            return null;
        }

        return student.getName() + " " + student.getFirstName();
    }

    /**
     * Cherche la table de numéro num, vérifie si elle est supprimée ou imposée, sinon entre dans
     * la logique de suppression de la table
     * @param num le numéro de la table
     * @return un entier adapté à la situation (géré côté Servlet et JavaScript)
     */
    public int deleteTable(int num) {
        num = findTable(num) ? num : -1;

        if (num == -1) {
            return -2;
        } else {
            if (d.isDeleted(num))
                return -3;

            else if (d.isImposed(num)) {
                return -4;
            } else {
                return d.removeTable(num);
            }
        }
    }

    /**
     * tables libres
     * @return les tables libres (non supprimées et sans étudiant/contrainte de place imposée)
     */
    public int[] free(){return d.freeTables();}

    /**
     * Entre dans la logique de réinsertion de table supprimée pour la table num
     * @param num numéro de la table
     */
    public void undeleteTable(int num) {
        d.undeleteTable(num);
    }

    /**
     * Définition des dimensions du plan
     * @param lon longueur du plan
     * @param lar largeur du plan
     */
    public void setDimensions(int lon, int lar) {
        d.setDimensions(lon, lar);
    }



    /**
     * Fonction de réinitialisation des données
     */
    public void resetData() {
        d.reset();
    }

    /**
     * Fonction de récupération de l'étudiant assis à la table num
     * @param num le numéro de la table
     * @return l'étudiant assis à la table num
     */
    public Student stuFromTable(int num) {
        return d.getStuFromTab(num);
    }


    public void setMode(int i) {
        switch (i) {
            case 0 -> d.changeMode('N');
            case 1 -> d.changeMode('G');
            case 2 -> d.changeMode('S');
        }
    }


    /**
     * Fonction de génération du contrôleur de positionnement
     * @return un objet PositioningIntermediate
     */
    public PositioningIntermediate generatePos() {
        return new PositioningIntermediate(d);
    }

    /**
     * Charge un plan grâce à un path
     * @param path chemin du plan à charger
     */
    public void loadDefaultMap(String path) {
        d.loadDefaultPlan(path);
    }

    /**
     * contrôleur du type de plan choisi
     * @param newOne caractère permettant le choix du type de plan
     * @param path chemin du plan
     */
    public void changeMapMode(char newOne, String path) {
        d.changePlanMode(newOne, path);
    }

    /**
     * Base pour la vision et modification de la table
     * @param oldNum ancien numéro de la table
     * @param newNum nouveau numéro de la table
     * @param numEtu numéro de l'étudiant
     * @return une chaine de caractères à utiliser sur la vision et modification de la table
     */
    public String tableValidateButton(int oldNum, int newNum, String numEtu) {
        String result = "";
        if (oldNum != 0 && tableExist(oldNum) && newNum > 0 && !tableExist(newNum)) {
            if (d.changeNumTable(oldNum, newNum)) {
                result += newNum + ";";
            } else {
                result += "error;";
            }
        } else if (oldNum != 0) {
            result += "invalid;";
        }

        if (!numEtu.isEmpty() && findTable(newNum)){
            result += findNumsForImp(numEtu,newNum);
        }else {
            result += "";
        }

        return result;
    }

    /**
     * vérifie que la table numTab existe
     * @param numTab le numéro de la table
     * @return true si la table existe, false sinon
     */
    public boolean tableExist(int numTab) {
        return d.tableExist(numTab);
    }

    /**
     * Fonction de récupération des étudiants séparés
     * @return une chaine de caractères contenant les informations des étudiants séparés
     */
    public String getSeparated() {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < 10; i++) {
            PerGroup temp = d.getPerGroup(i);
            if (temp == null) {
                break;
            } else {
                String[] students = temp.toString().split(";");
                for (String s : students) {
                    if (s.equals("null")) {
                        String id = findStudent(s) ;
                        result.append(id).append(":").append(d.getFullName(id)).append(";");
                    }
                }
                result.append("!");
            }
        }

        return result.toString();
    }

    /**
     * Affichage des étudiants dans une fenêtre popup
     * @return un string avec la liste d'étudiants
     */
    public String getStudentList() {
        String result = "ID             ; nom prenom \n";
        result += d.studentList();
        return result;
    }

    public String getDimensions() {
        return d.getPlanSize();
    }
}
