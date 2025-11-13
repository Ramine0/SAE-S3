package org.NeoMalokVector.SAE_S3;

public class Student
{
    public int group, subGroup;
    public String name, firstName, id;

    public Student(int group, int subGroup, String name, String firstName, String id)
    {
        this.group = group;
        this.subGroup = subGroup;

        this.name = name;
        this.firstName = firstName;

        this.id = id;
    }

    public String descrip(boolean inline ) {
        String result = "L'etudiant d'id "+id ;
        result +=  inline ? "\n" : " " ;
        result += "est "+ name + " " + firstName;
        result +=  inline ? "\n" : " " ;
        result += "du groupe "+ group +"."+ subGroup;
        return  result ;

    }
}
