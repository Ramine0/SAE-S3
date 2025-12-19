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
    public final ArrayList<Student> students = new ArrayList<>();
    int idC;
    // on laisse utiliser parfaitement les etus car c'est bcp plus pratique car il y a bcp de traitement a faire
    // notement avec les methodes qui sont assez nombreuses

    public Data()
    {
    }

    public void init()
    {
        constraints = new Constraint[students.size()];
        idC = 0;

        tables = new Table[students.size()]; // Quand le js sera fini, faudra changer la taille
        for (int i = 0; i < tables.length; i++)
            tables[i] = new Table();
    }

    private int[] deletedTables;

    public void placeStudent(int table, String idStudent)
    {
        tables[table - 1].setStudent(getStudentFromId(idStudent));
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
    public boolean isDeleted(int numTable)
    {
        return (Utilitaire.in(numTable, deletedTables));
    }

    public Table getTable(int numTable)
    {
        return tables[numTable];
    }

    public int[] getDeleted()
    {
        return deletedTables;
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

        String[] rest = new String[numPla];
        int numRest = 0;
        for (int i = 0; i < tables.length; i++)
        {
            if (!Utilitaire.in(students.get(i).getId(), place))
            {
                rest[numRest] = tables[i].getEtu().getId();
                numRest++;
            }
        }
        return rest;
    }

    public Student getStuFromTab(int num)
    {
        return tables[num].getEtu();
    }

    // renvoie les numeros de tables disponibles
    public int[] existingTables()
    {
        int[] result = new int[tables.length - deletedTables.length];
        int numRes = 0; // la position dans les resultats
        for (int i = 0; i < tables.length; i++)
        {
            // je verifie que ma table soit pas supprimée
            if (!Utilitaire.in(i, deletedTables))
            {
                // si c ok je l'ajoute a la liste
                result[numRes] = i;
                numRes++;
            }
        }
        return result;
    }

    public int[] freeTables()
    {
        int[] free = new int[tables.length - deletedTables.length];
        int numRes = 0;
        for (int i = 0; i < tables.length; i++)
        {
            if (Utilitaire.in(i, existingTables()) && tables[i].getEtu() == null)
            {
                free[numRes] = i;
                numRes++;
            }
        }
        return free;
    }

    public void removeTable(int num)
    {
        int i = tables.length - existingTables().length - 1;
        deletedTables[i] = num;
    }

    public void unremoveTable(int num)
    {
        for (int n : deletedTables)
        {
            if (deletedTables[n] == num)
            {
                deletedTables[n] = -1;
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

    public Student findStudent(String idStudent)
    {
        return null;
    }

    public Student[] getEtus()
    {
        return students.toArray(new Student[0]);
    }

    // le chargement du fichier exel donné par le/la prof
    public void chargerFichier(String filePath) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new FileReader(filePath + "/etudiants.csv"));
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
        tables = new Table[num];
        for (int i = 0; i < tables.length; i++)
            tables[i] = new Table();
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
        int cnt = 0;
        for (int i = 0; i < constraints.length; i++)
        {
            if (constraints[i] instanceof PerGroup)
            {
                if (cnt == id)
                {
                    return (PerGroup) constraints[i];
                } else
                {
                    cnt++;
                }
            }
        }
        return null;
    }

    public ImposedPlacement getImposedPlacement(int id)
    {
        int cnt = 0;
        for (int i = 0; i < constraints.length; i++)
        {
            if (constraints[i] instanceof ImposedPlacement)
            {
                if (cnt == id)
                {
                    return (ImposedPlacement) constraints[i];
                } else
                {
                    cnt++;
                }
            }
        }
        return null;
    }

    /// Il faudra peut-être modifié tout ça en fonction de la manière dont les contraintes sont indiquées
    public boolean addStudentGroupConstraint(String numStudent, int idGp){
        if (getPerGroup(idGp) != null){
            if (getPerGroup(idGp).haveStu(numStudent)) { return false; }
            getPerGroup(idGp).addStudent(numStudent);
            return true ;
        }
        return false ;
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
        if (id < idC)
        {
            for (int i = id; i < idC; i++)
            {
                if (i == idC - 1)
                {
                    constraints[i] = null;
                } else
                {
                    constraints[i] = constraints[i + 1];
                }
            }
            idC--;
        }
    }

    public void modifStudentGroupConstraint(String numStudent, int idGp, int idStu)
    {
        if (getPerGroup(idGp) != null)
        {
            getPerGroup(idGp).modifStudent(numStudent, idStu);
        }
    }

    public void removeStudentGroupConstraint(int idGp, int idStu)
    {
        if (getPerGroup(idGp) != null)
        {
            getPerGroup(idGp).removeStudent(idStu);
        }
    }

    public String completeId(String incomplet) {
        String possib ="" ;
        if (incomplet.startsWith("p") && students.get(0).getId().startsWith("1")){
            incomplet = "1"+ incomplet.substring(1);
        } else if (incomplet.startsWith("1") && students.get(0).getId().startsWith("p")){
            incomplet = "p"+ incomplet.substring(1);
        }

        for (Student s : students) {
            if (s.getId() == incomplet) {
                return incomplet;
            }else if(possib != "" && s.getId().startsWith(incomplet)) {
                return "" ;
            }else if (s.getId().startsWith(incomplet)) {
                possib = s.getId() ;
            }
        }
        return possib ;
    }

    public boolean addGrp () {
        return addConstraint( null,  0, 'N') ;
    }

}
