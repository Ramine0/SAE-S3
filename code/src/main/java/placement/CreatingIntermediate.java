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

    /**
     * Constructeur par défaut de la classe.
     *
     * @throws FileNotFoundException envoyée par Data() si elle ne trouve pas de fichier
     */
    public CreatingIntermediate() throws FileNotFoundException {
        d = new Data();
    }

    /**
     * Constructeur de la classe. À quoi il sert, bonne question???
     *
     * @param path le chemin vers le fichier
     * @throws FileNotFoundException envoyée par Data() si elle ne trouve pas le fichier
     */
    public CreatingIntermediate(String path) throws FileNotFoundException {
        d = new Data(path, "D");
    }

    /**
     * Contrôle de création d'un plan rectangulaire
     *
     * @param lon nombre de tables en longueur
     * @param lar nombre de tables en largeur
     */
    public void createTables(int lon, int lar) {
        d.setNumberTables(lon, lar);
    }

    /**
     * Fonction de récupération du nombre de tables
     *
     * @return le nombre de tables de la salle
     */
    public int getNumberOfTables() {
        return d.getTables().length;
    }

    /**
     * Fonction de récupération du numéro de table minimum
     *
     * @return le numéro de table le plus faible
     */
    public int minimumTableNumber() {
        return d.minimumTableNumber();
    }

    /**
     * Fonction de récupération du numéro de table maximum
     *
     * @return le numéro de table le plus important
     */
    public int maximumTableNumber() {
        return d.maximumTableNumber();
    }

    /**
     * Fonction de recherche d'un étudiant à partir de son identifiant
     *
     * @param studentId numéro étudiant, complet ou non
     * @return le numéro étudiant complété ou un message adapté
     */
    public String findStudent(String studentId) {
        String result = d.completeId(studentId);

        if (!result.isEmpty())
            return result;
        else
            return studentId.length() == 8 ? null : "1";
    }

    /**
     * Vérifie que la table tableNumber soit dans les tables libres
     *
     * @param tableNumber le numéro de la table
     * @return true si elle fait partie des tables libres, false sinon
     */
    public boolean findTable(int tableNumber) {
        return Utilitaire.in(tableNumber, free());
    }


    /**
     * Regarde si les informations studentId et tableNumber sont correctement renseignées, si c'est le cas appelle
     * la logique d'ajout de place imposée de Data
     *
     * @param studentId   numéro de l'étudiant
     * @param tableNumber numéro de la table
     * @return un entier correspondant à la situation
     */
    public int imposeStudent(String studentId, int tableNumber) {
        studentId = findStudent(studentId);
        tableNumber = findTable(tableNumber) ? tableNumber : -1;

        return studentId == null || studentId.length() > 8 || tableNumber == -1 ? -1 : d.imposeStudent(studentId, tableNumber);
    }

    /**
     * Retire une contrainte
     *
     * @param type type de la contrainte
     * @param id   index de la contrainte
     */
    public void removeConstraint(String type, int id) {
        d.removeConstraint(type, id);
    }

    /**
     * Vérifie que les informations soient correctement renseignées, si c'est le cas, entre dans
     * la logique d'ajout d'étudiant au groupe groupNumber
     *
     * @param incompleteId numéro de l'étudiant, complet ou non
     * @param groupNumber  groupe auquel on veut ajouter l'étudiant
     * @return en cas de succès, le message de la logique dans Data, sinon l'id partiel
     */
    public String separeStudentsPerGroup(String incompleteId, int groupNumber) {
        String student = findStudent(incompleteId);

        return student.length() == 8 ? d.addStudentGroupConstraint(student, groupNumber) : student;
    }

    /**
     * Fonction utilisée pour afficher la description des données
     *
     * @return la description des données
     */
    public String[] describeData() {
        return d.describe();
    }

    /**
     * Fonction de récupération de l'étudiant de numéro étudiant num
     *
     * @param num numéro de l'étudiant
     * @return null si l'étudiant n'est pas trouvé, son nom et son prénom sinon
     */
    public String studentInfo(String num) {
        Student student = d.getStudentFromId(num);

        if (student == null)
            return null;

        return student.getName() + " " + student.getFirstName();
    }

    /**
     * Cherche la table de numéro tableNumber, vérifie si elle est supprimée ou imposée, sinon entre dans
     * la logique de suppression de la table
     *
     * @param tableNumber le numéro de la table
     * @return un entier adapté à la situation (géré côté Servlet et JavaScript)
     */
    public int removeTable(int tableNumber) {
        tableNumber = findTable(tableNumber) ? tableNumber : -1;

        if (tableNumber == -1)
            return -2;
        else if (d.isDeleted(tableNumber))
            return -3;
        else if (d.isImposed(tableNumber))
            return -4;
        else
            return d.deleteTable(tableNumber);
    }

    /**
     * tables libres
     *
     * @return les tables libres (non supprimées et sans étudiant/contrainte de place imposée)
     */
    public int[] free() {
        return d.freeTables();
    }

    /**
     * Entre dans la logique de réinsertion de table supprimée pour la table tableNumber
     *
     * @param tableNumber numéro de la table
     */
    public void undeleteTable(int tableNumber) {
        d.undeleteTable(tableNumber);
    }

    /**
     * Définition des dimensions du plan
     *
     * @param width  longueur du plan
     * @param height largeur du plan
     */
    public void setDimensions(int width, int height) {
        d.setDimensions(width, height);
    }


    /**
     * Fonction de réinitialisation des données
     */
    public void resetData() {
        d.reset();
    }

    /**
     * Fonction de récupération de l'étudiant assis à la table tableNumber
     *
     * @param tableNumber le numéro de la table
     * @return l'étudiant assis à la table tableNumber
     */
    public Student getStudentFromTable(int tableNumber) {
        return d.getStudentFromTable(tableNumber);
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
     *
     * @return un objet PositioningIntermediate
     */
    public PositioningIntermediate generatePos() {
        return new PositioningIntermediate(d);
    }

    /**
     * Charge un plan grâce à un path
     *
     * @param path chemin du plan à charger
     */
    public void loadDefaultMap(String path) {
        d.loadDefaultPlan(path);
    }

    /**
     * contrôleur du type de plan choisi
     *
     * @param newOne caractère permettant le choix du type de plan
     * @param path   chemin du plan
     */
    public void changeMapMode(char newOne, String path) {
        d.changePlanMode(newOne, path);
    }

    /**
     * Base pour la vision et modification de la table
     *
     * @param oldNumber     ancien numéro de la table
     * @param newNumber     nouveau numéro de la table
     * @param studentNumber numéro de l'étudiant
     * @return une chaine de caractères à utiliser sur la vision et modification de la table
     */
    public String tableValidateButton(int oldNumber, int newNumber, String studentNumber) {
        String result = "";

        if (oldNumber != 0 && doesTableExist(oldNumber) && newNumber > 0 && !doesTableExist(newNumber))
            result += d.changeNumTable(oldNumber, newNumber) ? newNumber + ";" : "error;";
        else if (oldNumber != 0)
            result += "invalid;";

        result += !studentNumber.isEmpty() && findTable(newNumber) ? Integer.valueOf(imposeStudent(studentNumber, newNumber)) : "";

        return result;
    }

    /**
     * Vérifie que la table tableNumber existe
     *
     * @param tableNumber le numéro de la table
     * @return true si la table existe, false sinon
     */
    public boolean doesTableExist(int tableNumber) {
        return d.doesTableExist(tableNumber);
    }

    /**
     * Fonction de récupération des étudiants séparés
     *
     * @return une chaine de caractères contenant les informations des étudiants séparés
     */
    public String getSeparatedStudents() {
        StringBuilder result = new StringBuilder();

        for (int i = 1; i < 10; i++) {
            PerGroup temp = d.getPerGroup(i);

            if (temp == null)
                break;
            else {
                String[] students = temp.toString().split(";");

                for (String s : students)
                    if (!s.equals("null")) {
                        String id = findStudent(s);
                        result.append(id).append(":").append(d.getFullName(id)).append(";");
                    }

                result.append("!");
            }
        }

        return result.toString();
    }

    /**
     * Affichage des étudiants dans une fenêtre popup
     *
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
