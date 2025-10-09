public class Student
{
    public int number;
    public String name, surname;
    public String group, subgroup;

    public Student(int number, String name, String surname, String group, String subgroup)
    {
        this.number = number;

        this.name = name;
        this.surname = surname;

        this.group = group;
        this.subgroup = subgroup;
    }

    @Override
    public String toString()
    {
        return name + " (numéro: " + number + ")";
    }
}
