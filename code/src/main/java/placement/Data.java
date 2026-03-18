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
import java.util.Objects;
import java.util.Scanner;

public class Data {
    private Constraint[] constraints;
    private Table[] tables;
    private int[] deletedTables;

    private Map map;




    private final ArrayList<Student> students = new ArrayList<>();
    private int idC;

    public Data(String path, String mapType) throws FileNotFoundException {
        chargerFichier(path);

        if (mapType.charAt(0) == 'R') {
            // plan rectangulaire
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)), Character.getNumericValue(mapType.charAt(2)));
        } else if (mapType.charAt(0) == 'D') {
            map = new GridMap();
            loadPlanDefault(path);
        }


        init();
    }

    public Data() throws FileNotFoundException {
        map = new GridMap();
        loadPlanDefault("src/main/webapp/");
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


    public boolean placeStudent(int table, String idStudent) {
        if (getTable(table)== null ) {
            return false ;
        }
        getTable(table).setStudent(getStudentFromId(idStudent));
        return true ;
    }


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
        return Objects.requireNonNull(getTable(num)).getEtu();
    }

    public int nbDeletedTables() {
        int num = 0;
        if (tables.length==1) {
            return 100;
        }
        for (int deletedTable : deletedTables) {
            if (deletedTable != 0) {
                num++;
            }
        }
        return num;
    }

    public int[] existingTables() {
        int[] result = new int[tables.length - nbDeletedTables()];
        int numRes = 0;
        for (Table table : tables) {
            if (table != null) {
                // je verifie que ma table soit pas supprimée
                if (!isDeleted(table.getNum())) {
                    // si c'est bon, je l'ajoute à la liste
                    result[numRes] = table.getNum();
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

        for (Table table : tables) {
            if (Utilitaire.in(table.getNum(), existingTables()) && table.getEtu() == null) {
                length++;
            }
        }
        free = new int[length];
        for (Table table : tables) {
            if (Utilitaire.in(table.getNum(), existingTables()) && table.getEtu() == null) {
                free[numRes] = table.getNum();
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

    // voir si c'est pas remplacable
    public Constraint[] getConstr() {
        Constraint[] constr=new Constraint[getNbConstraint()];
        int i=0;
        for (Constraint c : constraints){
            constr[i]=c;
            i++;
        }
        return constr;
    }


    // ne pas supprimer, sauf si on ne lui trouve aucune utilité
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
        for (int i = 0; i < maxNumTable(); i++) {
            lesNums[i] = tables[i].getNum();
        }
        return lesNums;
    }

    public Student[] getStudents() {
        return students.toArray(new Student[0]);
    }

    private void chargerFichier() throws FileNotFoundException {
        chargerFichier("src/main/webapp/");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
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

    public void loadStudents(String s){
        String [] tab=s.split(";");
        for (String string : tab) {
            String[] student = string.replace(",", ";").split(";");
            students.add(new Student(student[3], student[4], student[1], student[2], student[0]));
        }
    }

    public void loadTables(String t){
        String [] tab=t.split(";");
        tables=new Table[tab.length];
        deletedTables=new int[tab.length];
        int i=0;
        for (String string : tab) {
            String[] table = string.replace(",", ";").split(";");
            tables[i]=new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), getStudentFromId(table[4]));
            if (Integer.parseInt(table[3])==1){
                removeTable(Integer.parseInt(table[0]));
            }
            i++;
        }
    }

    public void loadConstraints(String c){
        String [] tab=c.split(";");
        for (String string : tab) {
            String[] contrainte = string.replace(",", ";").split(";");
            if (contrainte[0].equals("G")) {
                addStudentGroupConstraint(contrainte[1], Integer.parseInt(contrainte[4]));
            } else {
                addImp(contrainte[1], Integer.parseInt(contrainte[2]));
            }
        }
    }


    public void placerImposes() {
        String[] s;

        for (Constraint c : constraints) {
            if (c instanceof ImposedPlacement) {
                s = ((ImposedPlacement) c).getImposed();
                placeStudent(Integer.parseInt(s[0]), s[1]);

            }
        }
    }

    public void setNumberTables(int lon, int lar) {
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

        }
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

    /// Il faudra peut-être modifier tout ça en fonction de la manière dont les contraintes sont indiquées
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

    public int getNbConstraint(){
        int nb=0;
        for (Constraint c : constraints) {
            if (c!=null){
                nb++;
            }
        }
        return nb;
    }

    public int getNbConstraint(String type) {
        int nb = 0;
        switch (type) {
            case "I" -> {
                for (int i = 0; i < idC; i++) {
                    if (constraints[i] instanceof ImposedPlacement) {
                        nb++;
                    }
                }
            }
            case "G" -> {
                for (int i = 0; i < idC; i++) {
                    if (constraints[i] instanceof PerGroup) {
                        nb++;
                    }
                }
            }
            case "M" -> {
                if (constraints[0] != null) {
                    nb++;
                }
            }
        }
        return nb;
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

    // à revoir, la façon dont c'est fait me semble très suspecte
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

    public void setDimensions(int lon, int lar) {
        map = new RectangularMap(lon, lar);
    }

    // je prends les voisins de ma table
    public Student[] neighbours(int t) {
        ArrayList<Student> voisins = new ArrayList<>();
        // pour tous les voisins de la map

        for (int i : map.neighbours(t, existingTables())) {
            //je récupère l'etu de la table si on a bien une table
            if (i != -1) {
                if (getTable(i) != null) {
                    voisins.add(getStuFromTab(i));
                }
            }

        }

        return voisins.toArray(new Student[0]);
    }


    public String[] imposedStudents() {
        String[] result = new String[getNbConstraint("I")];
        int i = 0;
        for (Constraint c : constraints) {
            if (c instanceof ImposedPlacement) {
                result[i] = ((ImposedPlacement) c).getNumStudent();
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
        return Objects.requireNonNull(getTable(numTable)).description();
    }

    public String getTablesInfos(){
        StringBuilder tab = new StringBuilder();
        for (Table t: tables){
            if (!tab.isEmpty()){
                tab.append(";");
            }
            tab.append(t.info());
        }
        return tab.toString();
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

    // la fonction est cool, je me demande pourquoi elle n'est pas used
    public boolean haveStudent(int tab) {
        return (!isDeleted(tab)) && (Objects.requireNonNull(getTable(tab)).getEtu() != null);
    }


    public boolean swap(int numT1, int numT2) {
        if (getStuFromTab(numT1) != null || getStuFromTab(numT2) != null) {
            Student temp = getStuFromTab(numT1);
            Objects.requireNonNull(getTable(numT1)).setStudent(getStuFromTab(numT2));
            Objects.requireNonNull(getTable(numT2)).setStudent(temp);
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

    public void loadPlanDefault(String path) {
        if (map instanceof GridMap)
            tables = ((GridMap) map).loadMap(path);

    }

    public void changePlanMode(char newMode, String path) {

        if (newMode == 'R') {
            if (map instanceof GridMap) {
                map = new RectangularMap(4, 4);
            }
        } else if (newMode == 'G') {
            if (map instanceof RectangularMap) {
                map = new GridMap(tables);
            }
        } else if (newMode == 'D') {
            map = new GridMap();
            loadPlanDefault(path);
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

    public boolean tableExist(int numTab) {
        return Utilitaire.in(numTab, existingTables());
    }


    public boolean changeNumTable(int oldNum, int newNum) {
        if (getTable(oldNum) != null ){
            Objects.requireNonNull(getTable(oldNum)).setNum(newNum);
            return true ;
        }
        return false;
    }

    public String studentList() {
        StringBuilder result = new StringBuilder();
        for (Student s : students) {
            result.append(s.getId()).append(" ; ").append(s.getFullName()).append("\n");
        }
        return result.toString();
    }


    // TODO ou la faire ou changer le code a vecteur pour utiliser student list qui est au dessus
    public Student[] getEtus() {
        return null ;
    }
}
