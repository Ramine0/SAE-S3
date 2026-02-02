package constraints;

import org.NeoMalokVector.SAE_S3.Student;
import utilitaire.Utilitaire;

public class PerGroup extends Constraint
{
    private final String[] groupe;
    private final int num ;

    public int getNum() {return num ;}

    public PerGroup(String etu, int numGrp)
    {
        num =  numGrp;
        groupe = new String[9] ;
        addStudent(etu);

    }
    public void addStudent(String numetu){
        int id=-1;
        for (int i=0; i<groupe.length; i++){
            if (groupe[i]==null && id==-1){
                id=i;
            }
        }
        groupe[id]=numetu;

        studentsConstraints.add(numetu);
    }
    public void modifStudent(String numetu, int index){
        groupe[index]=numetu;
    }
    public void removeStudent(int index){
        groupe[index]=null;
    }

    @Override
    public boolean validate(Student student, int table, Student[] etu)
    {
        if (Utilitaire.in(student.getId(), groupe))
        {
            for (Student s : etu)
            {
                if (s != null && Utilitaire.in(s.getId(), groupe))
                {

                    return false;
                }
            }
        }
        return true;
    }

    public boolean haveStu (String id) {
        return Utilitaire.in(id,groupe) ;
    }

    public int getNbStudent(){
        int nb=0;
        while (groupe[nb]!=null){
            nb++;
        }
        return nb;
    }

    public String getStudent(int index){
        return groupe[index];
    }
}
