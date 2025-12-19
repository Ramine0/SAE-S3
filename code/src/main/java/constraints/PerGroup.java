package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

public class PerGroup extends Constraint
{
    private String[] groupe;
    private int id;

    public PerGroup(String[] etus)
    {
        groupe = etus;
        id=0;
    }
    public void addStudent(String numetu){
        groupe[id]=numetu;
        id++;
    }
    public void modifStudent(String numetu, int index){
        groupe[index]=numetu;
    }
    public void removeStudent(int index){
        for (int i=index; i<groupe.length; i++){
            if (i==groupe.length-1){
                groupe[i]=null;
            }else{
                groupe[i]=groupe[i+1];
            }
        }
    }

    @Override
    public boolean validate(Student student, int table, Student[] etu)
    {
        if (Utilitaire.in(student, studentsConstraints.toArray()))
        {
            for (Student s : etu)
            {
                if (Utilitaire.in(s, studentsConstraints.toArray()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public  boolean haveStu (String id) {
        return Utilitaire.in(id,studentsConstraints.toArray()) ;
    }

}
