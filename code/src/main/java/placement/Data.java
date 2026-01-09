package placement;


import constraints.Constraint;
import constraints.ImposedPlacement;
import constraints.PerClass;
import constraints.PerGroup;
import org.NeoMalokVector.SAE_S3.Student;
import org.NeoMalokVector.SAE_S3.Table;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


// la classe va save les données pour les creer via un CreatingIntermediate et les avoir dans le positioning intermediate
// elle aura plein de fonction utiles comme le lecture du fichier des etus ou le traitement des etus

public class Data
{
    private Constraint[] constraints;
    private Table[] tables;
    private int[] deletedTables;
    private int nbImposed = 0;

    private Map map;


    public int[] getDeletedTables()
    {
        return deletedTables;
    }

    public final ArrayList<Student> students = new ArrayList<>();
    int idC;
    // on laisse utiliser parfaitement les etus car c'est bcp plus pratique car il y a bcp de traitement a faire
    // notement avec les methodes qui sont assez nombreuses

    public Data(String path, String mapType) throws FileNotFoundException
    {
        chargerFichier(path);

        if (mapType.charAt(0) == 'R')
        {
            // plan rectangulaire
            map = new RectangularMap(Character.getNumericValue(mapType.charAt(1)), Character.getNumericValue(mapType.charAt(2)));
        }

        init();
    }

    public Data() throws FileNotFoundException
    {
        chargerFichier();
        init();
    }

    private void init()
    {

        constraints = new Constraint[students.size()];
        idC = 0;
        nbImposed = 0;
    }


    public void placeStudent(int table, String idStudent) {getTable(table).setStudent(getStudentFromId(idStudent));}
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
    public boolean isDeleted(int numTable)
    {
        return (Utilitaire.in(numTable, deletedTables));
    }

    public String[] freeStudents()
    {
        String[] place = new String[students.size()];
        int numPla = 0;

        for (Table table : tables)
            if (table.getEtu() != null)
            {
                place[numPla] = table.getEtu().getId();
                numPla++;
            }

        String[] rest = new String[students.size() - numPla];
        int numRest = 0;
        for (Student s : students)
        {
            if (!Utilitaire.in(s.getId(), place))
            {
                rest[numRest] = s.getId();
                numRest++;
            }
        }
        return rest;
    }

    public Student getStuFromTab(int num)
    {
        return getTable(num).getEtu();
    }

    // renvoie les numeros de tables disponibles
    public int[] existingTables()
    {
        int[] result = new int[tables.length];
        int numRes = 0; // la position dans les resultats
        for (int i = 0; i < tables.length; i++)
        {
            if (tables[i] != null) {
                // je verifie que ma table soit pas supprimée
                if (!Utilitaire.in(tables[i].getNum(), deletedTables)) {
                    // si c ok je l'ajoute a la liste
                    result[numRes] = tables[i].getNum();
                    numRes++;
                }
            }
        }
        return result;
    }

    public int[] freeTables()
    {
        int[] free = new int[tables.length];
        int numRes = 0;

        for (int i = 0; i < tables.length; i++)
        {
            if (Utilitaire.in(tables[i].getNum(), existingTables()) && tables[i].getEtu() == null)
            {
                free[numRes] = tables[i].getNum();
                numRes++;
            }
        }
        return free;
    }

    public boolean removeTable(int num)
    {
        for (int i : deletedTables)
        {
            if (deletedTables[i] == 0)
            {
                deletedTables[i] = num;
                return true;
            }
        }
        return false;
    }

    public void unremoveTable(int num)
    {
        for (int n : deletedTables)
        {
            if (deletedTables[n] == num)
            {
                deletedTables[n] = 0;
            }
        }
    }

    public Student getStudentFromId(String id)
    {
        for (Student student : students)
            if (student.getId().equals(id))
                return student;

        return null;
    }

    public Constraint[] getConstr()
    {
        return constraints;
    }

    public int[] getTables()
    {
        int[] lesNums = new int[tables.length];
        for (int i = 0; i < tables.length; i++)
        {
            lesNums[i] = tables[i].getNum();
        }
        return lesNums;
    }

    public Student[] getEtus()
    {
        return students.toArray(new Student[0]);
    }

    private void chargerFichier() throws FileNotFoundException
    {
        chargerFichier("src/main/webapp/");
    }

    // le chargement du fichier exel donné par le/la prof
    private void chargerFichier(String path) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new FileReader(path + "resources/etudiants.csv"));
        String[] line;

        String id, nom, prenom;
        int group, subGroup;

        while (sc.hasNextLine())
        {
            line = sc.nextLine().split(";");

            id = null;
            nom = null;
            prenom = null;
            group = -1;
            subGroup = -1;

            for (String s : line)
            {
                if (!Objects.equals(s, ""))
                {
                    int numericValue = Character.getNumericValue(s.charAt(s.length() - 1));

                    if (Character.isDigit(s.charAt(1)) && s.length() == 8)
                        id = s;
                    else if ((s.contains("1") || s.contains("2") || s.contains("3")) && group == -1)
                    {
                        if (s.contains("."))
                        {
                            group = Character.getNumericValue(s.charAt(s.length() - 3));
                            subGroup = numericValue;
                        } else
                            group = numericValue;
                    } else if (s.contains("1") || s.contains("2") && group != -1 && subGroup == -1)
                        subGroup = numericValue;
                    else if (nom == null && !s.contains("@"))
                        nom = s;
                    else if (nom != null && prenom == null && !s.contains("@"))
                        prenom = s;
                }
            }

            if (id != null)
                students.add(new Student(group, subGroup, nom, prenom, id));
        }
    }

    public String[] descrip()
    {
        String[] text = new String[students.size()];
        int i = 0;
        for (Student s : students)
        {
            text[i] = s.descrip(true);
            i++;
        }

        return text;
    }


    public void placerImposes()
    {
        String[] s;

        for (Constraint c : constraints)
        {
            if (c instanceof ImposedPlacement)
            {
                s = ((ImposedPlacement) c).getPaire();
                placeStudent(Integer.parseInt(s[0]), s[1]);

            }
        }
    }

    public void setNumberTables(int num)
    {
        if (tables != null)
        {
            tables = null; //jsp si on delete comme ça, en gros si le prof veut changer les dimensions, faut delete ce qu'il y avait avant
        }
        if (deletedTables != null)
        {
            deletedTables = null;
        }
        if (num >= students.size())
        {
            tables = new Table[num];
            for (int i = 0; i < tables.length; i++)
                tables[i] = new Table();
            deletedTables = new int[num];
        } else
        {
            tables = new Table[students.size()];
            for (int i = 0; i < tables.length; i++)
            {
                tables[i] = new Table();
            }
            deletedTables = new int[students.size()];
        }

    }

    public void changeMode(char mode)
    {
        if (mode == 'G')
        {
            constraints[0] = new PerClass(false);
        } else if (mode == 'S')
        {
            constraints[0] = new PerClass(true);
        } else
        {
            constraints[0] = null;
        }
    }

    public PerGroup getPerGroup(int id)
    {
        for (Constraint constraint : constraints)
        {
            if (constraint instanceof PerGroup)
            {
                if (((PerGroup) constraint).getNum()==id){return (PerGroup) constraint ;}
            }
        }
        return null;
    }

    public ImposedPlacement getImposedPlacement(int id)
    {
        int cnt = 0;
        for (Constraint c : constraints)
        {
            if (c instanceof ImposedPlacement)
            {
                if (cnt == id)
                {
                    return (ImposedPlacement) c;
                } else
                {
                    cnt++;
                }
            }
        }
        return null;
    }

    /// Il faudra peut-être modifié tout ça en fonction de la manière dont les contraintes sont indiquées
    public String addStudentGroupConstraint(String numStudent, int idGp)
    {
        if (getPerGroup(idGp) != null)
        {
            if (getPerGroup(idGp).haveStu(numStudent)) {
                return "2";
            }else {
                getPerGroup(idGp).addStudent(numStudent);
                return numStudent +";"+ getFullName(numStudent);
            }
        } else
        {
            if( addConstraint(numStudent, idGp, 'N') == 0) {
                return numStudent +";"+getFullName(numStudent);
            }else {
                return "1";
            }

        }

    }

    public void modifConstraint(String numStudent, int numTable, String constr, int id, int index)
    {
        if (constr.equals("PI"))
        {
            getImposedPlacement(id).set(numTable, numStudent);
        } else if (constr.charAt(1) == 'G')
        {
            getPerGroup(id).modifStudent(numStudent, index);
        } else
        {
            changeMode(constr.charAt(0));
        }
    }

    public void removeConstraint(String constr, int id)
    {
        if (constr.equals("I"))
        {
            if (id < idC)
            {
                for (int i = id; i < idC; i++)
                {
                    if (i == idC - 1)
                    {
                        if (constraints[i] instanceof ImposedPlacement)
                        {
                            nbImposed--;
                        }
                        constraints[i] = null;

                    } else
                    {
                        constraints[i] = constraints[i + 1];
                    }
                }
                idC--;
            }
        }
    }

    public void modifStudentGroupConstraint(String numStudent, int idGp, int idStu)
    {
        if (getPerGroup(idGp) != null)
        {
            getPerGroup(idGp).modifStudent(numStudent, idStu);
        }
    }

    public int addConstraint(String numStudent, int numTable, char constr)
    {
        if (constr == 'I')
        {
            if (!Utilitaire.in(numStudent, imposedStudents()))
            {
                if (!Utilitaire.in(numTable, imposedTables()))
                {
                    constraints[idC] = new ImposedPlacement(numTable, numStudent);
                    nbImposed++;
                    idC++;

                    return 0;
                } else
                    return 2;
            }

            return 1;
        } else if (constr == 'N')
        {

            constraints[idC] = new PerGroup(numStudent,numTable);
            idC++;

            return 0;
        }

        return 1;
    }


    public void removeStudentGroupConstraint(int idGp, int idStu)
    {
        if (getPerGroup(idGp) != null)
        {
            getPerGroup(idGp).removeStudent(idStu);
        }
    }

    public String completeId(String incomplet)
    {
        String possib = "";
        if (incomplet.startsWith("p") && students.getFirst().getId().startsWith("1"))
        {
            incomplet = "1" + incomplet.substring(1);
        } else if (incomplet.startsWith("1") && students.getFirst().getId().startsWith("p"))
        {
            incomplet = "p" + incomplet.substring(1);
        }

        for (Student s : students)
        {
            if (s.getId().equals(incomplet))
            {
                return incomplet;
            } else if ((!possib.isEmpty()) && s.getId().startsWith(incomplet))
            {
                return "";
            } else if (s.getId().startsWith(incomplet))
            {
                possib = s.getId();
            }
        }
        return possib;
    }


    public int addImp(String id, int num)
    {
        return addConstraint(id, num, 'I');
    }

    public boolean setDimensions(int lon, int lar)
    {
        map = new RectangularMap(lon, lar);
        return true;
    }

    // je prends les voisins de ma table
    public Student[] neighbours(int t)
    {
        ArrayList<Student> voisins = new ArrayList<>();
        // pour tous les voisins de la map
        for (int i : map.neighbours(t, freeTables()))
        {

            //je recupere l'etu de la table si on a bien une table
            if (i != -1)
            {
                voisins.add(getStuFromTab(i));
            }
        }

        return voisins.toArray(new Student[0]);
    }

    public Map getMap()
    {
        return map;
    }

    public String[] imposedStudents()
    {
        String[] result = new String[nbImposed];
        int i = 0;
        for (Constraint c : constraints)
        {
            if (c instanceof ImposedPlacement)
            {
                result[i] = ((ImposedPlacement) c).getNumEtu();
                i++;
            }
        }

        return result;
    }

    public int[] imposedTables()
    {
        int[] result = new int[nbImposed];
        int i = 0;
        for (Constraint c : constraints)
        {
            if (c instanceof ImposedPlacement)
            {
                result[i] = ((ImposedPlacement) c).getNumTable();
                i++;
            }
        }

        return result;
    }

    public void reset()
    {
        Table.reset();
        Constraint.reset();
        init();
    }


    public String getTableInfos(int numTable) {
        return getTable(numTable).description() ;
    }

    public Table getTable(int num) {
        for (Table tb : tables) {
            if (tb.getNum() == num) {
                return tb;
            }
        }

        return null ;
    }

    public int maxNumTable() {
        return Utilitaire.max(freeTables());
    }

    public String getFullName(String id) {
        Student etu = getStudentFromId(id) ;
        return etu.getName() +" "+etu.getFirstName() ;
    }

    public boolean haveStudent(int tab) {
        return (! isDeleted(tab)) && (getTable(tab).getEtu() != null) ;
    }


    public boolean swap(int numT1, int numT2) {
        if (getStuFromTab(numT1) != null || getStuFromTab(numT2) != null ) {
            Student temp = getStuFromTab(numT1) ;
            getTable(numT1).setStudent(getStuFromTab(numT2));
            getTable(numT2).setStudent(temp);
            return true;
        }
        return false ;
    }

    public int maxTableID() {
        int max = 0;
        for (Table t : tables) {
            if (t.getNum() > max) {
                max = t.getNum();
            }
        }
        return max ;
    }
    public String getInfosForVisu(int num) {
        Student etu = getStuFromTab(num) ;
        return num +";"+ etu.getId()+";"+etu.getName()+" "+etu.getFirstName() ;
    }

}
