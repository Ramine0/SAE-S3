package org.NeoMalokVector.SAE_S3;

public class Student
{
    private final String name;
    private final String firstName;
    private final String id;
    private final String group;
    private final String subGroup;

    public Student(String group, String subGroup, String name, String firstName, String id)
    {

        this.group = group;
        this.subGroup = subGroup;
        this.name = name;
        this.firstName = firstName;

        this.id = id;
    }

    public String description(boolean inline)
    {
        String description = "L'étudiant d'id " + id;
        description += inline ? "\n" : ";";
        description += "est " + name + " " + firstName;
        description += inline ? "\n" : ";";
        description += "du groupe " + (group == null ? "groupe introuvable" : group) + "." + (subGroup == null ? "groupe introuvable" : subGroup);

        return description;

    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getFullName()
    {
        return name + " " + firstName;
    }

    public boolean sameGroup(Student etu, boolean sub)
    {
        if (etu.group.equals(this.group))
        {
            if (sub)
            {
                return subGroup.equals(etu.subGroup);
            }
            return true;
        }
        return false;
    }

    public String textVisualisation() {
        if (group  != null ) {
            return getId() + ";" + getName() + " " + getFirstName() + ";"+(group + "." + subGroup);
        }else {
            return getId() + ";" + getName() + " " + getFirstName() + ";"+"null" ;
        }
    }


}
