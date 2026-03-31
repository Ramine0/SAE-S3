package org.the_disabled.sae_s3;

public class Student {
    private final String id;

    private final String name;
    private final String firstName;

    private final String group;
    private final String subgroup;

    public Student(String group, String subgroup, String name, String firstName, String id) {
        this.id = id;

        this.name = name;
        this.firstName = firstName;

        this.group = group;
        this.subgroup = subgroup;
    }

    public String describe(boolean inline) {
        String description = "L'étudiant d'id " + id;
        description += inline ? "\n" : ";";
        description += "est " + name + " " + firstName;
        description += inline ? "\n" : ";";
        description += "du groupe " + (group == null ? "groupe introuvable" : group) + "." + (subgroup == null ? "groupe introuvable" : subgroup);

        return description;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return name + " " + firstName;
    }

    public boolean sameGroup(Student etu, boolean sub) {
        if (etu.group.equals(group)) {
            if (sub)
                return subgroup.equals(etu.subgroup);

            return true;
        }

        return false;
    }

    public String textVisualisation() {
        if (group != null)
            return getId() + ";" + getName() + " " + getFirstName() + ";" + (group + "." + subgroup);
        else
            return getId() + ";" + getName() + " " + getFirstName() + ";" + "null";
    }
}
