package org.NeoMalokVector.SAE_S3;

public class Student
{
    private int group, subGroup;
    private String name, firstName, id;

    public Student(int group, int subGroup, String name, String firstName, String id)
    {

        this.group = group;
        this.subGroup = subGroup;


        this.name = name;
        this.firstName = firstName;

        this.id = id;
    }

    public String descrip(boolean inline)
    {
        String result = "L'etudiant d'id " + id;
        result += inline ? "\n" : " ";
        result += "est " + name + " " + firstName;
        result += inline ? "\n" : " ";
        result += "du groupe " + group + "." + subGroup;
        return result;

    }

    public String getId()
    {
        return id;
    }

    public boolean sameGroup(Student etu, boolean sub)
    {
        if (etu.group == this.group)
        {
            if (sub)
            {
                return subGroup == etu.subGroup;
            }
        }
        return etu.group == this.group;
    }

}
