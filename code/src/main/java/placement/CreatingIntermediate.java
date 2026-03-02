package placement;

import constraints.Constraint;
import constraints.PerGroup;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;

/**
 * Classe utilisée comme controlleur de la création des données (contraintes, étudiants...)
 */
public class CreatingIntermediate
{
    private final Data d;

    /**
     * Constructeur par défaut de la classe.
     * @throws FileNotFoundException envoyée par Data() si elle ne trouve pas de fichier
     */
    public CreatingIntermediate() throws FileNotFoundException
    {
        d = new Data();
    }

    /**
     * Constructer de la classe. A quoi il sert, bonne question???
     * @param path le chemin vers d'autres données?
     * @throws FileNotFoundException envoyée par Data() si elle ne trouve pas de fichier
     */
    public CreatingIntermediate(String path) throws FileNotFoundException
    {
        d = new Data(path, "D");
    }

    /**
     * Contrôle de création d'un plan rectangulaire
     * @param lon nombre de tables en longueurs
     * @param lar nombre de tables en largeurs
     * @return true si la création est un succès, false sinon.
     */
    public boolean createTables(int lon, int lar)
    {
        return d.setNumberTables(lon, lar);
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
    public String findEtu(String id)
    {
        String trouve = d.completeId(id);
        if (!trouve.isEmpty())
        {
            return trouve;
        } else if (id.length() == 8)
        {
            return "le num donné n'existe pas";
        } else
        {
            return "1";
        }

    }

    /**
     * Vérifie que la table numTab soit dans les tables libres
     * @param numTab le numéro de la table
     * @return true si elle fait partie des tables libres, false sinon
     */
    public boolean findTable(int numTab)
    {
        return Utilitaire.in(numTab, d.freeTables());
    }


    /**
     * Regarde si les informations id et num sont correctements renseignées, si c'est le cas appelle
     * la logique d'ajout de places imposées de Data
     * @param id numéro de l'étudiant
     * @param num numéro de la table
     * @return un entier correspondant à la situation
     */
    public int findNumsForImp(String id, int num)
    {
        id = findEtu(id);
        num = findTable(num) ? num : -1;

        if (id.equals("le num donné n'existe pas"))
        {
            return -1;
        } else if (id.length() > 8)
        {
            return -1;
        } else if (num == -1)
        {
            return -1;
        } else
        {
            return d.addImp(id, num);
        }
    }

    /**
     * Retire la contrainte de type constr et d'index id
     * @param constr type de la contrainte
     * @param id index de la contrainte
     */
    public void removeContrainst(String constr, int id)
    {
        d.removeConstraint(constr, id);
    }

    /**
     * Renvoie le tableau de contraintes
     * @return le tableau de contraintes
     */
    public Constraint[] getConstr(){
        return d.getConstr();
    }

    /**
     * Vérifie que les informations soient correctemen renseignées, si c'est le cas entre dans
     * la logique d'ajout d'étudiant au groupe numGrp
     * @param idPartiel numéro de l'étudiant, complet ou non
     * @param numGrp groupe auquel on veut ajouter l'étudiant
     * @return en cas de succès le message de la logique dans data, sinon l'idPartiel
     */
    public String findStudentForGroup(String idPartiel, int numGrp)
    {
        String etu = findEtu(idPartiel);

        if (etu.length() == 8)
        {
            return d.addStudentGroupConstraint(etu, numGrp);
        } else
        {
            return etu;
        }
    }

    /**
     * fonction utilisée pour afficher la description des données
     * @return la description des données
     */
    public String[] descripData()
    {
        return d.descrip();
    }

    /**
     * Fonction de récupération de l'étudiant de numéro étudiant num
     * @param num numéro de l'étudiant
     * @return null si l'étudiant n'est pas trouvé, son nom et son prénom sinon
     */
    public String studentInfo(String num)
    {
        Student student = d.getStudentFromId(num);
        if (student == null)
        {
            return null;
        }

        return student.getName() + " " + student.getFirstName();
    }

    /**
     * Cherche la table de numéro num, vérifie si elle est supprimée ou imposée, sinon entre dans
     * la logique de suppression de la table
     * @param num le numéro de la table
     * @return un entier adapté à la situation (géré côté servlet et js)
     */
    public int supprTable(int num)
    {
        num = findTable(num) ? num : -1;

        if (num == -1)
        {
            return -2;
        } else
        {
            if (d.isDeleted(num))
                return -3;

            else if (d.isImposed(num)){
                return -4;
            }else {
                return d.removeTable(num) ;
            }
        }
    }

    /**
     * tables libres
     * @return les tables libres (non supprimées et sans étudiants/contrainte de place imposée
     */
    public int[] free(){return d.freeTables();}

    /**
     * tables supprimées
     * @return les tables supprimées
     */
    public int[] del(){return d.getDeletedTables();}

    /**
     * tables existantes
     * @return les tables existantes (non supprimées)
     */
    public int[] existing(){return d.existingTables();}

    /**
     * entre dans la logique de réinsertion de table supprimées pour la table num
     * @param num numéro de la table
     */
    public void unremoveTable(int num)
    {
        d.unremoveTable(num);
    }

    /**
     * définition des dimensions du plan
     * @param lon longueur du plan
     * @param lar largeur du plan
     */
    public void setDimensions(int lon, int lar) {d.setDimensions(lon, lar);}

    /**
     * fonction de récupération de la map
     * @return la map
     */
    public Map getMap()
    {
        return d.getMap();
    }

    /**
     * fonction de récupération des étudiants imposés
     * @return un tableau contenant les numéros étuidants de tous les étudiants imposés
     */
    public String[] getImposedStud()
    {
        return d.imposedStudents();
    }

    /**
     * fonction de réinitialisation des données
     */
    public void resetData()
    {
        d.reset();
    }

    /**
     * fonction de récupération de l'étudiant assis à la table num
     * @param num le numéro de la table
     * @return l'étudiant assis à la table num
     */
    public Student StuFromTable(int num)
    {
        return d.getStuFromTab(num);
    }

    /**
     * fonction d'obtention du nombre de contraintes
     * @return la longueur
     */
    public int getNbConstr()
    {
        return d.getConstr().length;
    }

    public Constraint getConstr(int num)
    {
        return d.getConstr()[num - 1];
    }

    public Constraint getConstr(String type, int num){
        if (type.equals("I")){
            return d.getImposedPlacement(num);
        }else if (type.equals("G")){
            return d.getPerGroup(num);
        }else{
            return d.getConstr()[0];
        }
    }

    public void setMode(int i)
    {
        switch (i)
        {
            case 0 -> d.changeMode('N');
            case 1 -> d.changeMode('G');
            case 2 -> d.changeMode('S');
        }
    }


    public PositioningIntermediate generatePos()
    {
        return new PositioningIntermediate(d);
    }

    public boolean loadPlanDefault(String path) {
        return d.loadPlanDefault(path);
    }

    public boolean changePlanMode(char newOne, String path) {
        return d.changePlanMode(newOne, path);
    }

    public String tableValidateButton(int oldNum, int newNum, String numEtu) {
        String result = "";
        if (oldNum != 0 && tableExist(oldNum) && newNum > 0 && ! tableExist(newNum)) {
            if ( d.changeNumTable(oldNum, newNum) ) {
                result += newNum + ";";
            }else {
                result += "error;";
            }
        }else if (oldNum != 0 ){
            result+= "invalid;" ;
        }

        if (numEtu != ""  && findTable(newNum)){
            result += findNumsForImp(numEtu,newNum);
        }else {
            result += "";
        }

        return result ;
    }

    public boolean tableExist(int numTab) {
        return d.tableExist(numTab) ;
    }

    /**
     *
     * @return
     */
    public String getSeparated() {
        String result = "";
        for (int i = 1; i < 10 ; i++ ) {
            PerGroup temp = d.getPerGroup(i) ;
            if (temp == null) {
                break ;
            }else {
                String[] students = temp.toString().split(";") ;
                for (String s : students) {
                    if (! s.isEmpty()) {
                        String id = findEtu(s) ;
                        result += id+":"+d.getFullName(id)+";" ;
                    }
                }
                result += "!" ;
            }
        }

        return result ;
    }

    /**
     * affichage des étudiants utilisé dans une fenêtre popup
     * @return un string avec la liste d'étudiants
     */
    public String getStudentList() {
        String result = "ID             ; nom prenom \n";
        result += d.studentList() ;
        return result ;
    }
}
