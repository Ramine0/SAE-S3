package placement;


import constraints.Constraint;
import constraints.ImposedPlacement;
import constraints.PerClass;
import constraints.PerGroup;
import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


// la classe va save les données pour les creer via un CreatingIntermediate et les avoir dans le positioning intermediate
// elle aura plein de fonction utiles comme le lecture du fichier des etus ou le traitement des etus

public class Data {
    private Constraint[] constraints;
    private Table[] tables;
    private int[] deletedTables;

    private Map map;


    public int[] getDeletedTables() {
        return deletedTables;
    }

    private final ArrayList<Student> students = new ArrayList<>();
    int idC;
    // on laisse utiliser parfaitement les etus car c'est bcp plus pratique car il y a bcp de traitement a faire
    // notement avec les methodes qui sont assez nombreuses

    public Data(String path, String mapType) throws FileNotFoundException {
        chargerFichier(path);

        if (mapType.charAt(0) == 'R') {
            // plan rectangulaire
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)), Character.getNumericValue(mapType.charAt(2)));
        } else if (mapType.charAt(0) == 'G') {
            // plan grille
            //map = new GridMap() ;
        } else if (mapType.charAt(0) == 'D') {
            map = new GridMap();
        }


        init();
    }

    public Data() throws FileNotFoundException {
        map = new GridMap();
        chargerFichier();
        init();
    }

    private void init() {
        deletedTables = new int[students.size()];

        if (constraints != null)
            for (int i = 0; i < constraints.length; i++)
                if (constraints[i] != null && constraints[i] instanceof PerGroup)
                    ((PerGroup) constraints[i]).removeStudent(i);

        constraints = new Constraint[students.size() + 1];

        idC = 0;
    }


    public void placeStudent(int table, String idStudent) {
        getTable(table).setStudent(getStudentFromId(idStudent));
    }

    // liste des fonctions a implementer
    /*
    bool isDeleted(Table/int) FAIT
    int[] freeTables()  revoie un tableau de num de tables disponibles FAIT
    getTable(int) FAIT
    getDeleted() renvoie le tableau FAIT
    freeStudents() renvoie les etus qui sont pas deja placés FAIT
    removeTable() FAIT
    unremoveTable() FAIT
    getStudentFromId(string id) ; FAIT


     */
    public boolean isDeleted(int numTable) {
        return (Utilitaire.in(numTable, deletedTables));
    }

    public String[] freeStudents() {

        String[] place = new String[students.size()];
        int numPla = 0;

        for (Table table : tables)
            if (table.getEtu() != null) {
                place[numPla] = table.getEtu().getId();
                numPla++;
            }

        String[] rest = new String[students.size() - numPla];
        int numRest = 0;
        for (Student s : students) {
            if (!Utilitaire.in(s.getId(), place)) {
                rest[numRest] = s.getId();
                numRest++;
            }
        }
        return rest;
    }

    public Student getStuFromTab(int num) {
        return getTable(num).getEtu();
    }

    // renvoie les numeros de tables disponibles
    public int nbDeletedTables() {
        int num = 0;
        if (tables.length==1) {
            return 100 ;
        }
        for (int i = 0; i < deletedTables.length; i++) {
            if (deletedTables[i] != 0) {
                num++;
            }
        }
        return num;
    }

    public int[] existingTables() {
        int[] result = new int[tables.length - nbDeletedTables()];
        int numRes = 0; // la position dans les resultats
        for (int i = 0; i < tables.length; i++) {
            if (tables[i] != null) {
                // je verifie que ma table soit pas supprimée
                if (!isDeleted(tables[i].getNum())) {
                    // si c ok je l'ajoute a la liste
                    result[numRes] = tables[i].getNum();
                    numRes++;
                }
            }
        }
        return result;
    }

    public int[] freeTables() {
        int[] free;
        int length = 0;
        int numRes = 0;

        for (int i = 0; i < tables.length; i++) {
            if (Utilitaire.in(tables[i].getNum(), existingTables()) && tables[i].getEtu() == null) {
                length++;
            }
        }
        free = new int[length];
        for (int i = 0; i < tables.length; i++) {
            if (Utilitaire.in(tables[i].getNum(), existingTables()) && tables[i].getEtu() == null) {
                free[numRes] = tables[i].getNum();
                numRes++;
            }
        }
        return free;
    }

    public int removeTable(int num) {
        for (int i = 0; i < deletedTables.length; i++) {
            if (deletedTables[i] == 0) {
                deletedTables[i] = num;
                return num;
            }
        }
        return -1;
    }

    public void unremoveTable(int num) {
        for (int n = 0; n < deletedTables.length; n++) {
            if (deletedTables[n] == num) {
                deletedTables[n] = 0;
            }
        }
    }

    public Student getStudentFromId(String id) {
        for (Student student : students)
            if (student.getId().equals(id))
                return student;

        return null;
    }

    public Constraint[] getConstr() {
        return constraints;
    }

    public Constraint[] getConstr(String type) {
        int i = 0;
        Constraint[] constr;
        if (type.equals("I")) {
            constr = new Constraint[getNbConstraint("I")];
            for (Constraint constraint : constraints) {
                if (constraint instanceof ImposedPlacement) {
                    constr[i] = constraint;
                    i++;
                }
            }
        } else if (type.equals("G")) {
            constr = new Constraint[getNbConstraint("G")];
            for (Constraint constraint : constraints) {
                if (constraint instanceof PerGroup) {
                    constr[i] = constraint;
                    i++;
                }
            }
        } else {
            constr = new Constraint[1];
            constr[0] = constraints[0];
        }
        return constr;
    }

    public int[] getTables() {
        int[] lesNums = new int[tables.length];
        for (int i = 0; i < tables.length; i++) {
            lesNums[i] = tables[i].getNum();
        }
        return lesNums;
    }

    public Student[] getEtus() {
        return students.toArray(new Student[0]);
    }

    private void chargerFichier() throws FileNotFoundException {
        chargerFichier("src/main/webapp/");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    // le chargement du fichier exel donné par le/la prof
    private void chargerFichier(String path) throws FileNotFoundException {
        students.clear();

        Scanner sc = new Scanner(new FileReader(path + "resources/etudiants.csv"));
        String[] line;

        String id, nom, prenom, group, subGroup;
        int iid, inom, iprenom, igroup, isubgroup;
        iid = -1;
        inom = -1;
        iprenom = -1;
        igroup = -1;
        isubgroup = -1;

        while (sc.hasNextLine()) {
            line = sc.nextLine().split(";");
            id = null;
            nom = null;
            prenom = null;
            group = null;
            subGroup = null;
            for (int i = 0; i < line.length; i++) {
                if (line[i].equals("numero")) {
                    iid = i;
                } else if (line[i].equals("nom")) {
                    inom = i;
                } else if (line[i].equals("prenom")) {
                    iprenom = i;
                } else if (line[i].equals("groupe")) {
                    igroup = i;
                } else if (line[i].equals("sous-groupe")) {
                    isubgroup = i;
                } else if (iid != -1 && inom != -1 && iprenom != -1 && igroup != -1) {
                    id = line[iid];
                    nom = line[inom];
                    prenom = line[iprenom];
                    if (isubgroup == -1) {
                        String[] groupInfo = line[igroup].replace(".", ";").split(";");
                        group = groupInfo[0];
                        if (groupInfo.length > 1) {
                            subGroup = groupInfo[1];
                        }
                    } else {
                        group = line[igroup];
                        subGroup = line[isubgroup];
                    }
                }
            }
            if (id != null && nom != null && group != null && prenom != null) {
                students.add(new Student(group, subGroup, nom, prenom, id));
            }
        }
        sc.close();
    }


    public String[] descrip() {
        String[] text = new String[students.size()];
        int i = 0;
        for (Student s : students) {
            text[i] = s.descrip(true);
            i++;
        }

        return text;
    }

    public void chargerStudents(String s){
        String [] tab=s.split(";");
        for (String string : tab) {
            String[] student = string.replace(",", ";").split(";");
            students.add(new Student(student[3], student[4], student[1], student[2], student[0]));
        }
    }

    public void chargerTables(String t){
        String [] tab=t.split(";");
        tables=new Table[tab.length];
        deletedTables=new int[tab.length];
        int i=0;
        for (String string : tab) {
            String[] table = string.replace(",", ";").split(";");
            tables[0]=new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), getStudentFromId(table[3]));
        }
    }


    public void placerImposes() {
        String[] s;

        for (Constraint c : constraints) {
            if (c instanceof ImposedPlacement) {
                s = ((ImposedPlacement) c).getPaire();
                placeStudent(Integer.parseInt(s[0]), s[1]);

            }
        }
    }

    public boolean setNumberTables(int lon, int lar) {
        int num = lon * lar;

        Table.reset();

        // il faut que ca soit une rectangular map
        if (map instanceof RectangularMap) {
            if (deletedTables != null) {
                deletedTables = null;
            }
            if (num >= students.size()) {
                tables = new Table[num];

                for (int i = 0; i < tables.length; i++)
                    tables[i] = new Table(i % lar + 1, i / lar + 1);

                deletedTables = new int[num];

            } else {
                tables = new Table[students.size()];

                for (int i = 0; i < tables.length; i++)
                    tables[i] = new Table(i % lar + 1, i / lar + 1);

                deletedTables = new int[students.size()];
            }
            return true;

        }
        return false;
    }

    public void changeMode(char mode) {
        if (idC == 0) {
            idC = 1;
        }
        if (mode == 'G') {
            constraints[0] = new PerClass(false);
        } else if (mode == 'S') {
            constraints[0] = new PerClass(true);
        } else {
            constraints[0] = null;
        }
    }

    public PerGroup getPerGroup(int id) {
        for (Constraint constraint : constraints) {
            if (constraint instanceof PerGroup) {
                if (((PerGroup) constraint).getNum() == id) {
                    return (PerGroup) constraint;
                }
            }
        }
        return null;
    }

    public int getPerGroupIndex() {
        int num = 1;
        boolean valide = false;
        while (!valide && num < getNbConstraint("G")) {
            valide = true;
            for (PerGroup pg : ((PerGroup[]) getConstr("G"))) {
                if (pg.getNum() == num) {
                    num++;
                    valide = false;
                }
            }
        }
        if (valide) {
            return num;
        } else {
            return -1;
        }
    }

    public int getIndexConstraint(String type, int id) {
        if (type.equals("I") && getImposedPlacement(id) != null) {
            for (int i = 0; i < idC; i++) {
                if (constraints[i] == getImposedPlacement(id)) {
                    return i;
                }
            }
        } else if (type.equals("G") && getPerGroup(id) != null) {
            for (int i = 0; i < idC; i++) {
                if (constraints[i] == getPerGroup(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ImposedPlacement getImposedPlacement(int id) {
        int cnt = 1;
        for (Constraint c : constraints) {
            if (c instanceof ImposedPlacement) {
                if (cnt == id) {
                    return (ImposedPlacement) c;
                } else {
                    cnt++;
                }
            }
        }
        return null;
    }

    /// Il faudra peut-être modifié tout ça en fonction de la manière dont les contraintes sont indiquées
    public String addStudentGroupConstraint(String numStudent, int idGp) {
        if (getPerGroup(idGp) != null) {
            if (getPerGroup(idGp).haveStu(numStudent)) {
                return "2";
            } else {
                getPerGroup(idGp).addStudent(numStudent);
                return numStudent + ";" + getFullName(numStudent);
            }
        } else {
            if (addConstraint(numStudent, idGp, 'N') == 0) {
                return numStudent + ";" + getFullName(numStudent);
            } else {
                return "1";
            }

        }

    }

    public void removeConstraint(String constr, int id) {
        if (constr.equals("I")) {
            if (id >= 0 && id < idC && getImposedPlacement(id) != null) {
                for (int i = getIndexConstraint(constr, id); i < idC; i++) {
                    if (i == idC - 1) {
                        constraints[i] = null;
                    } else {
                        constraints[i] = constraints[i + 1];
                    }
                }
                idC--;
            }
        } else if (constr.equals("G")) {
            if (id < idC && getPerGroup(id) != null) {
                for (int i = getIndexConstraint(constr, id); i < idC; i++) {
                    if (i == idC - 1) {
                        constraints[i] = null;
                    } else {
                        constraints[i] = constraints[i + 1];
                    }
                }
                idC--;
            }
        } else {
            if (id >= 0 && id < idC && getPerGroup(id) != null) {
                getPerGroup(id).removeStudent(Integer.parseInt(constr));
            }
        }
    }

    public int getNbConstraint(String type) {
        int nb = 0;
        if (type.equals("I")) {
            for (int i = 0; i < idC; i++) {
                if (constraints[i] instanceof ImposedPlacement) {
                    nb++;
                }
            }
        } else if (type.equals("G")) {
            for (int i = 0; i < idC; i++) {
                if (constraints[i] instanceof PerGroup) {
                    nb++;
                }
            }
        } else if (type.equals("M")) {
            if (constraints[0] != null) {
                nb++;
            }
        }
        return nb;
    }

    public void modifStudentGroupConstraint(String numStudent, int idGp, int idStu) {
        if (getPerGroup(idGp) != null) {
            getPerGroup(idGp).modifStudent(numStudent, idStu);
        }
    }

    public int addConstraint(String numStudent, int numTable, char constr) {
        if (constr == 'I') {
            if (!Utilitaire.in(numStudent, imposedStudents())) {
                if (!Utilitaire.in(numTable, imposedTables())) {
                    if (idC != 0) {
                        constraints[idC] = new ImposedPlacement(numTable, numStudent);
                        idC++;

                        return 0;
                    }
                } else {
                    return 2;
                }
            }


            return 1;
        } else if (constr == 'N') {
            if (idC != 0) {
                constraints[idC] = new PerGroup(numStudent, numTable);
                idC++;
                return 0;
            }
        }

        return 1;
    }


    public void removeStudentGroupConstraint(int idGp, int idStu) {
        if (getPerGroup(idGp) != null) {
            getPerGroup(idGp).removeStudent(idStu);
        }
    }

    public String completeId(String incomplet) {
        String possib = "";
        if (incomplet.startsWith("p") && students.getFirst().getId().startsWith("1")) {
            incomplet = "1" + incomplet.substring(1);
        } else if (incomplet.startsWith("1") && students.getFirst().getId().startsWith("p")) {
            incomplet = "p" + incomplet.substring(1);
        }

        for (Student s : students) {
            if (s.getId().equals(incomplet)) {
                return incomplet;
            } else if ((!possib.isEmpty()) && s.getId().startsWith(incomplet)) {
                return "";
            } else if (s.getId().startsWith(incomplet)) {
                possib = s.getId();
            }
        }
        return possib;
    }


    public int addImp(String id, int num) {
        return addConstraint(id, num, 'I');
    }

    public boolean setDimensions(int lon, int lar) {
        map = new RectangularMap(lon, lar);
        return true;
    }

    // je prends les voisins de ma table
    public Student[] neighbours(int t) {
        ArrayList<Student> voisins = new ArrayList<>();
        // pour tous les voisins de la map

//        System.out.println(Arrays.toString(map.neighbours(t, freeTables())));
        for (int i : map.neighbours(t, existingTables())) {
            //je recupere l'etu de la table si on a bien une table
            if (i != -1) {
                if (getTable(i) != null) {
                    voisins.add(getStuFromTab(i));
                }
            }

        }

        System.out.println();

        return voisins.toArray(new Student[0]);
    }

    public Map getMap() {
        return map;
    }

    public String[] imposedStudents() {
        String[] result = new String[getNbConstraint("I")];
        int i = 0;
        for (Constraint c : constraints) {
            if (c instanceof ImposedPlacement) {
                result[i] = ((ImposedPlacement) c).getNumEtu();
                i++;
            }
        }

        return result;
    }

    public int[] imposedTables() {
        int[] result = new int[getNbConstraint("I")];
        int i = 0;
        for (Constraint c : constraints) {
            if (c instanceof ImposedPlacement) {
                result[i] = ((ImposedPlacement) c).getNumTable();
                i++;
            }
        }

        return result;
    }

    public void reset() {
        Table.reset();
        Constraint.reset();
        init();
    }


    public String getTableInfos(int numTable) {
        return getTable(numTable).description();
    }

    private Table getTable(int num) {

        for (Table tb : tables) {
            if (tb.getNum() == num) {
                return tb;
            }
        }

        return null;
    }

    public int minNumTable() {
        return Utilitaire.min(freeTables());
    }

    public int maxNumTable() {
        return Utilitaire.max(freeTables());
    }

    public String getFullName(String id) {
        Student etu = getStudentFromId(id);
        return etu.getFullName();
    }

    public boolean haveStudent(int tab) {
        return (!isDeleted(tab)) && (getTable(tab).getEtu() != null);
    }


    public boolean swap(int numT1, int numT2) {
        if (getStuFromTab(numT1) != null || getStuFromTab(numT2) != null) {
            Student temp = getStuFromTab(numT1);
            getTable(numT1).setStudent(getStuFromTab(numT2));
            getTable(numT2).setStudent(temp);
            return true;
        }
        return false;
    }

    public int maxTableID() {
        int max = 0;
        for (Table t : tables) {
            if (t.getNum() > max) {
                max = t.getNum();
            }
        }
        return max;
    }

    public String getInfosForVisu(int num) {
        if (getTable(num) != null && getStuFromTab(num) != null) {
            Student etu = getStuFromTab(num);
            return num + ";" + etu.textVisu();
        } else {
            return num + ";null;null;null";
        }
    }

    public boolean isImposed(int numTab) {
        return Utilitaire.in(numTab, imposedTables());
    }

    public boolean hasMode() {
        return constraints[0] != null;
    }

    public boolean loadPlanDefault(String path) {
        if (map instanceof GridMap) {
            tables = ((GridMap) map).loadMap(path);
            return tables != null;
        } else {
            return false;
        }
    }

    public boolean changePlanMode(char newMode, String path) {

        if (newMode == 'R') {
            if (map instanceof GridMap) {
                map = new RectangularMap(4, 4);
                return true;
            } else {
                return false;
            }
        } else if (newMode == 'G') {
            if (map instanceof RectangularMap) {
                map = new GridMap(tables);
                return true;
            } else {
                return false;
            }
        } else if (newMode == 'D') {
            map = new GridMap();
            return loadPlanDefault(path);
        }else {
            return false ;
        }
    }

    public int maxTableX() {
        int[] coords = new int[tables.length];
        int cpt = 0;
        for (Table tb : tables) {
            coords[cpt] = tb.getCoord()[0];
            cpt++;
        }
        return Utilitaire.max(coords);
    }

    public int maxTableY() {
        int[] coords = new int[tables.length];
        int cpt = 0;
        for (Table tb : tables) {
            coords[cpt] = tb.getCoord()[1];
            cpt++;
        }
        return Utilitaire.max(coords);
    }

    public String getPlanSize() {
        if (map instanceof GridMap) {
            return maxTableX() + ";" + maxTableY();
        } else if (map instanceof RectangularMap) {
            return ((RectangularMap) map).getSize();
        } else {
            return "";
        }
    }

    public boolean tableExist(int numTab) {return Utilitaire.in(numTab, existingTables()) ;}

    public boolean changeNumTable(int oldNum, int newNum) {
        if (getTable(oldNum) != null ){
            getTable(oldNum).setNum(newNum);
            return true ;
        }
        return false ;
    }
}
