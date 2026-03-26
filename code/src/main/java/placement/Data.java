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
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Data {
    private final ArrayList<Student> students = new ArrayList<>();

    private Table[] tables;
    private Constraint[] constraints;

    private Map map;

    private int idC;
    private int[] deletedTables;

    public Data(String path, String mapType) throws FileNotFoundException {
        loadFile(path);

        if (mapType.charAt(0) == 'R')
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)), Character.getNumericValue(mapType.charAt(2)));
        else if (mapType.charAt(0) == 'D') {
            map = new GridMap();
            loadDefaultPlan(path);
        }

        init();
    }

    public Data() throws FileNotFoundException {
        map = new GridMap();
        loadDefaultPlan("src/main/webapp/");

        loadFile();
        init();
    }

    private void init() {
        deletedTables = new int[students.size()];
        constraints = new Constraint[students.size() + 1];

        for (int i = 0; i < constraints.length; i++)
            if (constraints[i] != null && constraints[i] instanceof PerGroup)
                ((PerGroup) constraints[i]).removeStudent(i);

        idC = 0;
    }


    public void placeStudent(int table, String idStudent) {
        if (getTable(table) == null)
            return;
        Objects.requireNonNull(getTable(table)).setStudent(getStudentFromId(idStudent));
    }


    public boolean isDeleted(int numTable) {
        return (Utilitaire.in(numTable, deletedTables));
    }

    public String[] freeStudents() {
        List<String> result = new ArrayList<>();
        List<String> occupiedTables = new ArrayList<>();

        for (Table table : tables)
            if (table.getStudent() != null)
                occupiedTables.add(table.getStudent().getId());

        for (Student student : students)
            if (!Utilitaire.in(student.getId(), occupiedTables.toArray()))
                result.add(student.getId());

        return result.toArray(new String[0]);
    }

    public Student getStudentFromTable(int tableNumber) {
        return Objects.requireNonNull(getTable(tableNumber)).getStudent();
    }

    public int[] existingTables() {
        List<Integer> result = new ArrayList<>();

        for (Table table : tables)
            if (table != null && !isDeleted(table.getNumber()))
                result.add(table.getNumber());

        return result.stream().mapToInt(i -> i).toArray();
    }

    public int[] freeTables() {
        List<Integer> free = new ArrayList<>();

        for (Table table : tables)
            if (Utilitaire.in(table.getNumber(), existingTables()) && table.getStudent() == null)
                free.add(table.getNumber());

        return free.stream().mapToInt(i -> i).toArray();
    }

    public int deleteTable(int tableNumber) {
        for (int i = 0; i < deletedTables.length; i++)
            if (deletedTables[i] == 0) {
                deletedTables[i] = tableNumber;
                return tableNumber;
            }

        return -1;
    }

    public void undeleteTable(int tableNumber) {
        for (int i = 0; i < deletedTables.length; i++)
            if (deletedTables[i] == tableNumber)
                deletedTables[i] = 0;
    }

    public Student getStudentFromId(String id) {
        for (Student student : students)
            if (student.getId().equals(id))
                return student;

        return null;
    }

    public Constraint[] getConstraints() {
        return constraints;
    }

    public Constraint[] getConstraints(String type) {
        List<Constraint> result = new ArrayList<>();

        for (Constraint constraint : constraints)
            if ((type.equals("I") && constraint instanceof ImposedPlacement) || (type.equals("G") && constraint instanceof PerGroup))
                result.add(constraint);
            else {
                result.add(constraints[0]);
                break;
            }

        return result.toArray(new Constraint[0]);
    }

    public int[] getTables() {
        List<Integer> result = new ArrayList<>();

        for (Table table : tables)
            result.add(table.getNumber());

        return result.stream().mapToInt(i -> i).toArray();
    }

    private void loadFile() throws FileNotFoundException {
        loadFile("src/main/webapp/");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void loadFile(String path) throws FileNotFoundException {
        students.clear();

        Scanner sc = new Scanner(new FileReader(path + "resources/etudiants.csv"));

        int idIndex = -1;

        int nameIndex = -1;
        int firstNameIndex = -1;

        int groupIndex = -1;
        int subgroupIndex = -1;

        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");

            String id = null;

            String name = null;
            String firstName = null;

            String group = null;
            String subgroup = null;

            for (int i = 0; i < line.length; i++)
                if (line[i].equals("numero"))
                    idIndex = i;
                else if (line[i].equals("nom"))
                    nameIndex = i;
                else if (line[i].equals("prenom"))
                    firstNameIndex = i;
                else if (line[i].equals("groupe"))
                    groupIndex = i;
                else if (line[i].equals("sous-groupe"))
                    subgroupIndex = i;
                else if (idIndex != -1 && nameIndex != -1 && firstNameIndex != -1 && groupIndex != -1) {
                    id = line[idIndex];
                    name = line[nameIndex];
                    firstName = line[firstNameIndex];

                    if (subgroupIndex == -1) {
                        String[] groupInfo = line[groupIndex].replace(".", ";").split(";");

                        group = groupInfo[0];

                        if (groupInfo.length > 1)
                            subgroup = groupInfo[1];
                    } else {
                        group = line[groupIndex];
                        subgroup = line[subgroupIndex];
                    }
                }

            if (id != null && name != null && group != null && firstName != null)
                students.add(new Student(group, subgroup, name, firstName, id));
        }

        sc.close();
    }


    public String[] describe() {
        List<String> result = new ArrayList<>();

        for (Student s : students)
            result.add(s.describe(true));

        return result.toArray(new String[0]);
    }

    public void loadStudents(String studentsList) {
        for (String student : studentsList.split(";")) {
            String[] studentInfos = student.split(",");
            students.add(new Student(studentInfos[3], studentInfos[4], studentInfos[1], studentInfos[2], studentInfos[0]));
        }
    }

    public void loadTables(String tablesList) {
        String[] tables = tablesList.split(";");

        this.tables = new Table[tables.length];
        deletedTables = new int[tables.length];

        for (int i = 0; i < tables.length; i++) {
            String[] table = tables[i].split(",");
            this.tables[i] = new Table(Integer.parseInt(table[0]), Integer.parseInt(table[1]), Integer.parseInt(table[2]), getStudentFromId(table[4]));

            if (Integer.parseInt(table[3]) == 1)
                deleteTable(Integer.parseInt(table[0]));
        }
    }

    public void loadConstraints(String constraintsList) {
        String[] tab = constraintsList.split(";");

        for (String constraint : tab) {
            String[] constraintInfos = constraint.split(",");

            if (constraintInfos[0].equals("G"))
                addStudentGroupConstraint(constraintInfos[1], Integer.parseInt(constraintInfos[4]));
            else
                imposeStudent(constraintInfos[1], Integer.parseInt(constraintInfos[2]));
        }
    }


    public void placeImposedStudents() {
        for (Constraint constraint : constraints)
            if (constraint instanceof ImposedPlacement) {
                String[] student = ((ImposedPlacement) constraint).getImposed();
                placeStudent(Integer.parseInt(student[0]), student[1]);
            }
    }

    public void setNumberTables(int lon, int lar) {
        int num = lon * lar;

        Table.reset();

        if (map instanceof RectangularMap) {
            if (deletedTables != null)
                deletedTables = null;

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
        if (idC == 0)
            idC = 1;

        if (mode == 'G')
            constraints[0] = new PerClass(false);
        else if (mode == 'S')
            constraints[0] = new PerClass(true);
        else
            constraints[0] = null;
    }

    public PerGroup getPerGroup(int id) {
        for (Constraint constraint : constraints)
            if (constraint instanceof PerGroup)
                if (((PerGroup) constraint).getNumber() == id)
                    return (PerGroup) constraint;

        return null;
    }

    public int getPerGroupIndex() {
        int num = 1;
        boolean valide = false;
        while (!valide && num < getConstraintsNumber("G")) {
            valide = true;
            for (PerGroup pg : ((PerGroup[]) getConstraints("G")))
                if (pg.getNumber() == num) {
                    num++;
                    valide = false;
                }
        }
        if (valide)
            return num;
        else
            return -1;
    }

    public int getIndexConstraint(String type, int id) {
        if (type.equals("I") && getImposedPlacement(id) != null) {
            for (int i = 0; i < idC; i++)
                if (constraints[i] == getImposedPlacement(id))
                    return i;
        } else if (type.equals("G") && getPerGroup(id) != null)
            for (int i = 0; i < idC; i++)
                if (constraints[i] == getPerGroup(id))
                    return i;

        return -1;
    }

    public ImposedPlacement getImposedPlacement(int constraintId) {
        for (int i = 0; i < constraints.length; i++)
            if (constraints[i] instanceof ImposedPlacement && i + 1 == constraintId)
                return (ImposedPlacement) constraints[i];

        return null;
    }

    /// Il faudra peut-être modifier tout ça en fonction de la manière dont les contraintes sont indiquées
    public String addStudentGroupConstraint(String numStudent, int idGp) {
        if (getPerGroup(idGp) != null)
            if (getPerGroup(idGp).haveStudent(numStudent))
                return "2";
            else {
                getPerGroup(idGp).addStudent(numStudent);
                return numStudent + ";" + getFullName(numStudent);
            }
        else
            return addConstraint(numStudent, idGp, 'N') == 0 ? numStudent + ";" + getFullName(numStudent) : "1";
    }

    public void removeConstraint(String type, int constraintId) {
        if (type.equals("I")) {
            if (constraintId >= 0 && constraintId < idC && getImposedPlacement(constraintId) != null) {
                for (int i = getIndexConstraint(type, constraintId); i < idC; i++)
                    constraints[i] = i == idC - 1 ? null : constraints[i + 1];

                idC--;
            }
        } else if (type.equals("G")) {
            if (constraintId < idC && getPerGroup(constraintId) != null) {
                for (int i = getIndexConstraint(type, constraintId); i < idC; i++)
                    if (i == idC - 1)
                        constraints[i] = null;
                    else
                        constraints[i] = constraints[i + 1];
                idC--;
            }
        } else if (constraintId >= 0 && constraintId < idC && getPerGroup(constraintId) != null)
            getPerGroup(constraintId).removeStudent(Integer.parseInt(type));
    }

    public int getConstraintsNumber() {
        int nb = 0;

        for (Constraint c : constraints)
            if (c != null)
                nb++;

        return nb;
    }

    public int getConstraintsNumber(String type) {
        int result = 0;

        switch (type) {
            case "I" -> {
                for (int i = 0; i < idC; i++)
                    if (constraints[i] instanceof ImposedPlacement)
                        result++;
            }
            case "G" -> {
                for (int i = 0; i < idC; i++)
                    if (constraints[i] instanceof PerGroup)
                        result++;
            }
            case "M" -> {
                if (constraints[0] != null)
                    result++;
            }
        }

        return result;
    }


    public int addConstraint(String studentNumber, int tableNumber, char constraint) {
        if (constraint == 'I') {
            if (!Utilitaire.in(studentNumber, imposedStudents()))
                if (!Utilitaire.in(tableNumber, imposedTables())) {
                    if (idC != 0) {
                        constraints[idC] = new ImposedPlacement(tableNumber, studentNumber);
                        idC++;

                        return 0;
                    }
                } else
                    return 2;


            return 1;
        } else if (constraint == 'N')
            if (idC != 0) {
                constraints[idC] = new PerGroup(studentNumber, tableNumber);
                idC++;

                return 0;
            }

        return 1;
    }

    public void removeStudentGroupConstraint(int groupId, int studentId) {
        if (getPerGroup(groupId) != null)
            getPerGroup(groupId).removeStudent(studentId);
    }

    public String completeId(String incomplete) {
        String result = "";

        if (incomplete.startsWith("p") && students.getFirst().getId().startsWith("1"))
            incomplete = "1" + incomplete.substring(1);
        else if (incomplete.startsWith("1") && students.getFirst().getId().startsWith("p"))
            incomplete = "p" + incomplete.substring(1);

        for (Student s : students)
            if (s.getId().equals(incomplete))
                return incomplete;
            else if ((!result.isEmpty()) && s.getId().startsWith(incomplete))
                return "";
            else if (s.getId().startsWith(incomplete))
                result = s.getId();

        return result;
    }


    public int imposeStudent(String studentId, int tableNumber) {
        return addConstraint(studentId, tableNumber, 'I');
    }

    public void setDimensions(int width, int height) {
        map = new RectangularMap(width, height);
    }

    public Student[] neighbours(int tableNumber) {
        ArrayList<Student> result = new ArrayList<>();

        for (int i : map.neighbours(tableNumber, existingTables())) {


            if (i != -1)
                if (getTable(i) != null)
                    result.add(getStudentFromTable(i));
        }

        return result.toArray(new Student[0]);
    }


    public String[] imposedStudents() {
        List<String> result = new ArrayList<>();

        for (Constraint constraint : constraints)
            if (constraint instanceof ImposedPlacement)
                result.add(((ImposedPlacement) constraint).getStudentNumber());

        return result.toArray(new String[0]);
    }

    public int[] imposedTables() {
        List<Integer> result = new ArrayList<>();

        for (Constraint c : constraints)
            if (c instanceof ImposedPlacement)
                result.add(((ImposedPlacement) c).getTableNumber());

        return result.stream().mapToInt(i -> i).toArray();
    }

    public void reset() {
        Table.reset();
        Constraint.reset();
        init();
    }

    public String getTableInfos(int numTable) {
        return Objects.requireNonNull(getTable(numTable)).describe();
    }

    public String getTablesInfos() {
        StringBuilder tab = new StringBuilder();

        for (Table t : tables) {
            if (!tab.isEmpty())
                tab.append(";");

            tab.append(t.getInformations());
        }

        return tab.toString();
    }

    private Table getTable(int tableNumber) {

        for (Table table : tables)
            if (table.getNumber() == tableNumber)
                return table;

        return null;
    }

    public int minimumTableNumber() {
        return Utilitaire.min(freeTables());
    }

    public int maximumTableNumber() {
        return Utilitaire.max(freeTables());
    }

    public String getFullName(String studentId) {
        return getStudentFromId(studentId).getFullName();
    }

    // la fonction est cool, je me demande pourquoi elle n'est pas used
    public boolean haveStudent(int tab) {
        return (!isDeleted(tab)) && (Objects.requireNonNull(getTable(tab)).getStudent() != null);
    }


    public boolean swap(int numT1, int numT2) {
        if (getStudentFromTable(numT1) != null || getStudentFromTable(numT2) != null) {
            Student temp = getStudentFromTable(numT1);
            Objects.requireNonNull(getTable(numT1)).setStudent(getStudentFromTable(numT2));
            Objects.requireNonNull(getTable(numT2)).setStudent(temp);

            return true;
        }

        return false;
    }

    public int maxTableID() {
        int max = 0;

        for (Table t : tables)
            if (t.getNumber() > max)
                max = t.getNumber();

        return max;
    }

    public String getInformationsForVisualisation(int tableNumber) {
        return getTable(tableNumber) != null && getStudentFromTable(tableNumber) != null ? tableNumber + ";" + getStudentFromTable(tableNumber).textVisualisation() : tableNumber + ";null;null;null";
    }

    public boolean isImposed(int tableNumber) {
        return Utilitaire.in(tableNumber, imposedTables());
    }

    public boolean hasMode() {
        return constraints[0] != null;
    }

    public void loadDefaultPlan(String filePath) {
        tables = ((GridMap) map).loadMap(filePath);
    }

    public void changePlanMode(char newMode, String filePath) {
        if (newMode == 'R') {
            if (map instanceof GridMap)
                map = new RectangularMap(4, 4);
        } else if (newMode == 'D') {
            map = new GridMap();
            loadDefaultPlan(filePath);
        }
    }

    public int maxTableX() {
        List<Integer> result = new ArrayList<>();

        for (Table table : tables)
            result.add(table.getCoordinates()[0]);

        return Utilitaire.max(result.stream().mapToInt(i -> i).toArray());
    }

    public int maxTableY() {
        List<Integer> result = new ArrayList<>();

        for (Table table : tables)
            result.add(table.getCoordinates()[1]);

        return Utilitaire.max(result.stream().mapToInt(i -> i).toArray());
    }

    public String getPlanSize() {
        if (map instanceof GridMap)
            return maxTableX() + ";" + maxTableY();
        else if (map instanceof RectangularMap)
            return ((RectangularMap) map).getSize();
        else
            return "";
    }

    public boolean doesTableExist(int tableNumber) {
        return Utilitaire.in(tableNumber, existingTables());
    }


    public boolean changeNumTable(int oldNumber, int newNumber) {
        if (getTable(oldNumber) != null ){
            Objects.requireNonNull(getTable(oldNumber)).setNumber(newNumber);
            return true ;
        }

        return false;
    }

    public String studentList() {
        StringBuilder result = new StringBuilder();
        for (Student s : students)
            result.append(s.getId()).append(" ; ").append(s.getFullName()).append("\n");
        return result.toString();
    }


    public Student[] getStudents() {
        return students.toArray(new Student[0]);
    }
}
